using BusinessPlusData.Mapping;
using BusinessPlusData.Models;
using Microsoft.EntityFrameworkCore;
using System.Linq.Expressions;
using System.Security.Cryptography;
using System.Text;

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
            var hashedPassword = HashPassword(academia.Contrasena);
            return await _context.Academias
                .Where(a => a.Usuario == academia.Usuario && a.Contrasena == hashedPassword)
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstAsync();
        }

        public Academia GetAcademia()
        {
            return _context.Academias
                .Where(a => a.CodAcademia == 1)
                .Select(AcademiaMapping.MapToAcademia(_context))
                .First();
        }

        public async Task<Academia> ChangePasswordAsync(Academia academia)
        {
            var getAcademia = _context.Academias
                .Where(a => a.Usuario.Equals(academia.Usuario))
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstOrDefault();

            var hashedPassword = HashPassword(academia.Contrasena);
            getAcademia.Contrasena = hashedPassword;
            _context.Update(getAcademia);
            await _context.SaveChangesAsync();

            return await _context.Academias
                .Where(a => a.CodAcademia == getAcademia.CodAcademia)
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstAsync();
        }

        public async Task<Academia> EditAcademiaAsync(Academia academia)
        {
            var getAcademia = _context.Academias
                .Where(a => a.Usuario.Equals(academia.Usuario))
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstOrDefault();
            getAcademia.Usuario = academia.Usuario;
            getAcademia.Nombre = academia.Nombre;
            getAcademia.Email = academia.Email;
            getAcademia.Telefono = academia.Telefono;
            getAcademia.Latitud = academia.Latitud;
            getAcademia.Longitud = academia.Longitud;
            if (!string.IsNullOrWhiteSpace(getAcademia.Direccion))
            {
                getAcademia.Direccion = academia.Direccion;
            }
            getAcademia.ImgPerfil = academia.ImgPerfil;
            
            _context.Update(getAcademia);
            await _context.SaveChangesAsync();

            return await _context.Academias
                .Where(a => a.CodAcademia == getAcademia.CodAcademia)
                .Select(AcademiaMapping.MapToAcademia(_context))
                .FirstAsync();
        }

        public static string HashPassword(string password)
        {
            using (SHA256 sha256Hash = SHA256.Create())
            {
                byte[] bytes = sha256Hash.ComputeHash(Encoding.UTF8.GetBytes(password));

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < bytes.Length; i++)
                {
                    builder.Append(bytes[i].ToString("x2"));
                }
                return builder.ToString();
            }
        }
    }
}
