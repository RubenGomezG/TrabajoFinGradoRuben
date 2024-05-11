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

        public List<Curso> ListarCursosDeAcademia(Academia academia)
        {
            return _context.Cursos
                .Where(c => c.CodAcademia == academia.CodAcademia)
                .Select(CursoMapping.MapToCurso(_context))
                .ToList();
        }

        public async Task<Academia> ChangePasswordAsync(Academia academia)
        {
            var getAcademia = _context.Academias.Where(a => a.Usuario.Equals(academia.Usuario)).Select(AcademiaMapping.MapToAcademia(_context)).FirstOrDefault();
            getAcademia.Contrasena = academia.Contrasena;
            _context.Update(getAcademia);
            await _context.SaveChangesAsync();

            return await _context.Academias
                .Where(a => a.CodAcademia == getAcademia.CodAcademia)
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstAsync();
        }
    }
}
