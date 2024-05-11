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
                Direccion = a.Direccion,
                Latitud = a.Latitud,
                Longitud = a.Longitud,
                ImgPerfil = a.ImgPerfil,
                Cursos = a.Cursos.Select(ca => new Curso
                {
                    CodCurso = ca.CodCurso,
                    NombreCurso = ca.NombreCurso,
                    FechaInicioCurso = ca.FechaInicioCurso,
                    FechaFinCurso = ca.FechaFinCurso,
                    Precio = ca.Precio,
                    Valoracion = ca.Valoracion,
                    Descripcion = ca.Descripcion,
                    Tipo = ca.Tipo,
                    CodAcademia = ca.CodAcademia,
                }).ToList(),
                ConversacioneUsuario1s = a.ConversacioneUsuario1s.Select(ca => new Conversacione
                {
                    CodConversacion = ca.CodConversacion,
                    Usuario1Id = ca.Usuario1Id,
                    Usuario2Id = ca.Usuario2Id,
                }).ToList(),
            };
        }
    }
}
