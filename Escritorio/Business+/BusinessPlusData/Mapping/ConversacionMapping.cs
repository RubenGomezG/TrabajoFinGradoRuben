using BusinessPlusData.Models;
using System.Linq.Expressions;

namespace BusinessPlusData.Mapping
{
    public static class ConversacionMapping
    {
        public static Expression<Func<Conversacione, Conversacione>> MapToConversacion(Bu5x9ctsBusinessplusContext dbContext)
        {
            return c => new Conversacione
            {
                CodConversacion = c.CodConversacion,
                Usuario1Id = c.Usuario1Id,
                Usuario2Id = c.Usuario2Id,
                Usuario1 = c.Usuario1,
                Usuario2 = c.Usuario2,
                Mensajes = c.Mensajes.Select(mc => new Mensaje
                {
                    CodMensaje = mc.CodMensaje,
                    CodConversacion = mc.CodConversacion,
                    SenderUsername = mc.SenderUsername,
                    SenderAcademia = mc.SenderAcademia,
                    Contenido = mc.Contenido,
                    Timestamp = mc.Timestamp,
                    CodConversacionNavigation = mc.CodConversacionNavigation,
                    Sender = mc.Sender,
                    SenderCodAcademia = mc.SenderCodAcademia,
                }).ToList(),
            };
        }
    }
}
