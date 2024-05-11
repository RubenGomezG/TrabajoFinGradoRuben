using BusinessPlusData.Models;
using System.Linq.Expressions;

namespace BusinessPlusData.Mapping
{
    public static class CursoMapping
    {
        public static Expression<Func<Curso, Curso>> MapToCurso(Bu5x9ctsBusinessplusContext dbContext)
        {
            return c => new Curso
            {
                CodCurso = c.CodCurso,
                NombreCurso = c.NombreCurso,
                FechaInicioCurso = c.FechaInicioCurso,
                FechaFinCurso = c.FechaFinCurso,
                Precio = c.Precio,
                Valoracion = c.Valoracion,
                Descripcion = c.Descripcion,
                Tipo = c.Tipo,
                CodAcademia = c.CodAcademia,
            };
        }
    }
}
