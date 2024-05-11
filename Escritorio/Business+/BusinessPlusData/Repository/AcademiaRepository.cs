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
