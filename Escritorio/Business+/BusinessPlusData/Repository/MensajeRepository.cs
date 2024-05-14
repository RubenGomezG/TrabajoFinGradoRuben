using BusinessPlusData.Mapping;
using BusinessPlusData.Models;
using Microsoft.EntityFrameworkCore;

namespace BusinessPlusData.Repository
{
    public class MensajeRepository
    {
        private readonly Bu5x9ctsBusinessplusContext _context;

        public MensajeRepository(Bu5x9ctsBusinessplusContext context)
        {
            _context = context ?? throw new ArgumentNullException(nameof(context));
        }

        public async Task CreateMensajeAsync(Mensaje mensaje)
        {
            await _context.Mensajes.AddAsync(mensaje);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteMensajeAsync(int codMensaje)
        {
            var mensaje = await _context.Mensajes
                .Where(c => c.CodMensaje == codMensaje)
                .Select(MensajeMapping.MapToMensaje(_context))
                .FirstAsync();
            if (mensaje != null)
            {
                _context.Mensajes.Remove(mensaje);
                _context.Entry(mensaje).State = EntityState.Deleted;
                await _context.SaveChangesAsync();
            }
        }
    }
}
