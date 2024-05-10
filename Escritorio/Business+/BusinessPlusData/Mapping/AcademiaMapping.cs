using BusinessPlusData.Models;
using System.Linq.Expressions;

namespace BusinessPlusData.Mapping
{
    public static class AcademiaMapping
    {
        public static Expression<Func<Academia, Academia>> MapToAcademia(Bu5x9ctsBusinessplusContext dbContext)
        {
            return a => new Academia
            {
                CodAcademia = a.CodAcademia,
                Usuario = a.Usuario,
                Contrasena = a.Contrasena,
                Email = a.Email,
                Nombre = a.Nombre,
                Telefono = a.Telefono,
                Latitud = a.Latitud,
                Longitud = a.Longitud,
                ImgPerfil = a.ImgPerfil,
                Cursos = (from ca in a.Cursos
                           join c in dbContext.Cursos
                           on ca.CodAcademia equals c.CodAcademia
                           select new Curso
                           {
                               CodCurso = c.CodCurso,
                               NombreCurso = c.NombreCurso,
                               FechaInicioCurso = c.FechaInicioCurso,
                               FechaFinCurso = c.FechaFinCurso,
                               Precio = c.Precio,
                               Valoracion = c.Valoracion,
                               Descripcion = c.Descripcion,
                               Tipo = c.Tipo,
                               CodAcademia = c.CodAcademia
                           }).ToList()
            };
        }

    }
}
