using BusinessPlusData.Mapping;
using BusinessPlusData.Models;
using Microsoft.EntityFrameworkCore;
using System.Linq.Expressions;

namespace BusinessPlusData.Repository
{
    public class AcademiaRepository
    {
        private readonly Bu5x9ctsBusinessplusContext _context;

        public AcademiaRepository(Bu5x9ctsBusinessplusContext context)
        {
            _context = context ?? throw new ArgumentNullException(nameof(context));
        }

        public async Task<Academia> LoginAsync(Academia academia)
        {
            return await _context.Academias
                .Where(a => a.Usuario == academia.Usuario && a.Contrasena == academia.Contrasena)
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstAsync();
        }

        public async Task<Academia> ChangePasswordAsync(Academia academia)
        {
            var getAcademia = _context.Academias.Where(a => a.CodAcademia.Equals(academia.CodAcademia)).Select(AcademiaMapping.MapToAcademia(_context)).FirstOrDefault();
            getAcademia.Contrasena = academia.Contrasena;
            var nuevaAcademia = new Academia
            {
                CodAcademia = getAcademia.CodAcademia,
                Usuario = getAcademia.Usuario,
                Contrasena = getAcademia.Contrasena,
                Email = getAcademia.Email,
                Nombre = getAcademia.Nombre,
                Telefono = getAcademia.Telefono,
                Direccion = getAcademia.Direccion,
                Latitud = getAcademia.Latitud,
                Longitud = getAcademia.Longitud,
                ImgPerfil = getAcademia.ImgPerfil,
                Cursos = getAcademia.Cursos
            };
            _context.Academias.Add(nuevaAcademia);
            _context.Update(nuevaAcademia);
            await _context.SaveChangesAsync();

            return await _context.Academias
                .Where(a => a.CodAcademia == academia.CodAcademia)
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstAsync();
        }


    }
}
