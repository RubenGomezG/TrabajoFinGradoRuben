using BusinessPlusData.Models;
using System.Linq.Expressions;

namespace BusinessPlusData.Mapping
{
    public static class InscripcionesMapping
    {
        public static Expression<Func<Inscripcione, Inscripcione>> MapToInscripcione(Bu5x9ctsBusinessplusContext dbContext)
        {
            return i => new Inscripcione
            {
                CodCurso = i.CodCurso,
                Usuario = i.Usuario,
                CodCursoNavigation = i.CodCursoNavigation,
                UsuarioNavigation = i.UsuarioNavigation,
            };
        }
    }
}
