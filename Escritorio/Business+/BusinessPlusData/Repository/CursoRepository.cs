using BusinessPlusData.Mapping;
using BusinessPlusData.Models;
using Microsoft.EntityFrameworkCore;

namespace BusinessPlusData.Repository
{
    public class CursoRepository
    {
        private readonly Bu5x9ctsBusinessplusContext _context;

        public CursoRepository(Bu5x9ctsBusinessplusContext context)
        {
            _context = context ?? throw new ArgumentNullException(nameof(context));
        }

        public async Task<List<Curso>> ListarCursosDeAcademia(Academia academia)
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                return await context.Cursos
                .Where(c => c.CodAcademia == academia.CodAcademia)
                .Select(CursoMapping.MapToCurso(context))
                .ToListAsync();
            }
        }

        public async Task<List<Curso>> BuscarCursosPorNombreAsync(string texto)
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                return await context.Cursos
                .Where(c => c.NombreCurso.Contains(texto))
                .Select(CursoMapping.MapToCurso(context))
                .ToListAsync();
            } 
        }

        public async Task<Curso> GetCursoAsync(int id)
        {
            return await _context.Cursos
                .Where(c => c.CodCurso == id)
                .Select(CursoMapping.MapToCurso(_context))
                .FirstAsync();
        }

        public async Task CreateCursoAsync(Curso curso)
        {
            await _context.Cursos.AddAsync(curso);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteCursoAsync(int id)
        {
            var curso = await _context.Cursos
                .Where(c => c.CodCurso == id)
                .Select(CursoMapping.MapToCurso(_context))
                .FirstAsync();
            if (curso != null)
            {
                _context.Cursos.Remove(curso);
                _context.Entry(curso).State = EntityState.Deleted;
                await _context.SaveChangesAsync();
            }
        }

        public async Task<Curso> UpdateCursoAsync(Curso curso)
        {
            Curso getCurso = await _context.Cursos
                .Where(a => a.CodCurso == curso.CodCurso)
                .Select(CursoMapping.MapToCurso(_context))
                .FirstAsync();

            getCurso.NombreCurso = curso.NombreCurso;
            getCurso.FechaInicioCurso = curso.FechaInicioCurso;
            getCurso.FechaFinCurso = curso.FechaFinCurso;
            getCurso.Precio = curso.Precio;
            getCurso.Descripcion = curso.Descripcion;
            getCurso.Tipo = curso.Tipo;
            _context.Update(getCurso);
            await _context.SaveChangesAsync();

            return await _context.Cursos
                .Where(c => c.CodCurso == getCurso.CodCurso)
                .Select(CursoMapping.MapToCurso(_context))
                .FirstAsync();
        }
    }
}
