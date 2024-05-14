using BusinessPlusData.Models;
using System.Linq.Expressions;

namespace BusinessPlusData.Mapping
{
    public static class MensajeMapping
    {
        public static Expression<Func<Mensaje, Mensaje>> MapToMensaje(Bu5x9ctsBusinessplusContext dbContext)
        {
            return m => new Mensaje
            {
                CodMensaje = m.CodMensaje,
                CodConversacion = m.CodConversacion,
                SenderUsername = m.SenderUsername,
                SenderCodAcademia = m.SenderCodAcademia,
                Contenido = m.Contenido,
                Timestamp = m.Timestamp,
                CodConversacionNavigation = m.CodConversacionNavigation,
                Sender = m.Sender,
                SenderAcademia = m.SenderAcademia,
            };
        }
    }
}

