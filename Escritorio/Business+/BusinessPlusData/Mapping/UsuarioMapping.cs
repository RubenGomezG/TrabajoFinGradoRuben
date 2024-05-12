using BusinessPlusData.Models;
using System.Linq.Expressions;

namespace BusinessPlusData.Mapping
{
    public class UsuarioMapping
    {
        public static Expression<Func<Usuario, Usuario>> MapToUsuario(Bu5x9ctsBusinessplusContext dbContext)
        {
            return u => new Usuario
            {
                Usuario1 = u.Usuario1,
                Contrasena = u.Contrasena,
                Email = u.Email,
                Nombre = u.Nombre,
                Apellidos = u.Apellidos,
                Telefono = u.Telefono,
                ImgPerfil = u.ImgPerfil,
                Edad = u.Edad,
                ConversacioneUsuario2s = u.ConversacioneUsuario2s.Select(cu => new Conversacione
                {
                    CodConversacion = cu.CodConversacion,
                    Usuario1Id = cu.Usuario1Id,
                    Usuario2Id = cu.Usuario2Id,
                }).ToList(),
                Mensajes = u.Mensajes.Select(mu => new Mensaje
                {
                    CodMensaje = mu.CodMensaje,
                    CodConversacion = mu.CodConversacion,
                    SenderUsername = mu.SenderUsername,
                    SenderCodAcademia = mu.SenderCodAcademia,
                    Contenido = mu.Contenido,
                    Timestamp = mu.Timestamp,
                }).ToList(),
                Inscripciones = u.Inscripciones.Select(iu => new Inscripcione
                {
                    CodCurso = iu.CodCurso,
                    Usuario = iu.Usuario,
                    CodCursoNavigation = iu.CodCursoNavigation,
                    UsuarioNavigation = iu.UsuarioNavigation,
                }).ToList(),
            };
        }
    }
}
