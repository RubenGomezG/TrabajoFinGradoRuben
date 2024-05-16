using BusinessPlusData.Mapping;
using BusinessPlusData.Models;
using BusinessPlusData.ViewModels;
using Microsoft.EntityFrameworkCore;
using System.Collections.ObjectModel;

namespace BusinessPlusData.Repository
{
    public class ConversacionRepository
    {
        private readonly Bu5x9ctsBusinessplusContext _context;

        public ConversacionRepository(Bu5x9ctsBusinessplusContext context)
        {
            _context = context ?? throw new ArgumentNullException(nameof(context));
        }

        public List<ConversacionesViewModel> GetAllConversaciones()
        {
            var conversaciones = _context.Conversaciones
                .Include(c => c.Usuario1)
                .Include(c => c.Usuario2)
                .Include(c => c.Mensajes)
                    .ThenInclude(m => m.Sender)
                .Include(c => c.Mensajes)
                    .ThenInclude(m => m.SenderAcademia)
                .ToList();
            return conversaciones.Select(c =>
            {
                var ultimoMensaje = c.Mensajes.OrderByDescending(m => m.Timestamp).First();
                var nombreRemitente = ultimoMensaje.Sender != null ? ultimoMensaje.Sender.Nombre : "Yo";
                return new ConversacionesViewModel
                {
                    CodConversacion = c.CodConversacion,
                    NombreUsuario = c.Usuario2.Nombre + " " + c.Usuario2.Apellidos,
                    NombreRemitente = nombreRemitente,
                    UltimoMensaje = ultimoMensaje.Contenido,
                };
            })
            .ToList();
        }

        public List<Conversacione> ListarConversacionesDeAcademia()
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                return context.Conversaciones
                .Where(c => c.Usuario1Id == 1)
                .Select(ConversacionMapping.MapToConversacion(context)).ToList();
            }
        }

        public async Task<Conversacione> GetConversacionAsync(int id)
        {
            return await _context.Conversaciones
                .Where(c => c.CodConversacion == id)
                .Select(ConversacionMapping.MapToConversacion(_context))
                .FirstAsync();
        }

        public async Task<Conversacione> GetConversacionByUsernameAsync(string username)
        {
            return await _context.Conversaciones
                .Where(c => c.Usuario2Id == username)
                .Select(ConversacionMapping.MapToConversacion(_context))
                .FirstOrDefaultAsync();
        }

        public List<Mensaje> ListarMensajesDeConversacion(Conversacione conversacione)
        {
            var query = from conversacion in _context.Conversaciones
                        join mensaje in _context.Mensajes on conversacion.CodConversacion equals mensaje.CodConversacion
                        where mensaje.CodConversacion == conversacione.CodConversacion
                        select mensaje;

            return query.ToList();
        }

        public ObservableCollection<ChatViewModel> ListarMensajeViewModelDeConversacion(int codConversacion)
        {
            var query = from conversacion in _context.Conversaciones
                        join mensaje in _context.Mensajes on conversacion.CodConversacion equals mensaje.CodConversacion
                        where mensaje.CodConversacion == codConversacion
                        select new ChatViewModel
                        {
                            CodMensaje = mensaje.CodMensaje,
                            NombreRemitente = mensaje.Sender != null ? mensaje.Sender.Nombre : "Yo",
                            FechaMensaje = mensaje.Timestamp.ToString(),
                            Contenido = mensaje.Contenido,
                        };

            return new ObservableCollection<ChatViewModel>(query.ToList());
        }

        public Mensaje UltimoMensajeDeConversacion(Conversacione conversacione)
        {

                var query = from conversacion in _context.Conversaciones
                            join mensaje in _context.Mensajes on conversacion.CodConversacion equals mensaje.CodConversacion
                            where mensaje.CodConversacion == conversacione.CodConversacion
                            orderby mensaje.Timestamp descending
                            select mensaje;
                return query.First();
        }

        public void CreateConversacion(Conversacione conversacione)
        {
            _context.Conversaciones.Add(conversacione);
            _context.SaveChanges();
        }

        public async Task DeleteConversacionAsync(int id)
        {
            var conversacion = await _context.Conversaciones
                .Where(c => c.CodConversacion == id)
                .Select(ConversacionMapping.MapToConversacion(_context))
                .FirstAsync();
            if (conversacion != null)
            {
                _context.Conversaciones.Remove(conversacion);
                _context.Entry(conversacion).State = EntityState.Deleted;
                await _context.SaveChangesAsync();
            }
        }

        /*public List<string> ListarCursosDeUsuario(Usuario usuario)
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                var query = from inscripcion in context.Inscripciones
                            join curso in context.Cursos on inscripcion.CodCurso equals curso.CodCurso
                            where inscripcion.Usuario == usuario.Usuario1
                            select curso.NombreCurso;

                return query.ToList();
            }
        }*/
    }
}
