using BusinessPlusData.Mapping;
using BusinessPlusData.Models;
using Microsoft.EntityFrameworkCore;

namespace BusinessPlusData.Repository
{
    public class InscripcionesRepository
    {

        private readonly Bu5x9ctsBusinessplusContext _context;
        public InscripcionesRepository(Bu5x9ctsBusinessplusContext context)
        {
            _context = context ?? throw new ArgumentNullException(nameof(context));
        }
        public async Task<List<Inscripcione>> ListarInscripciones()
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                return await context.Inscripciones
                .Select(InscripcionesMapping.MapToInscripcione(context)).ToListAsync();
            }
        }

        public List<string> ListarUsuariosDeCurso(Curso curso)
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                var query = from inscripcion in context.Inscripciones
                            join usuario in context.Usuarios on inscripcion.Usuario equals usuario.Usuario1
                            where inscripcion.CodCurso == curso.CodCurso
                            select usuario.Nombre;

                return query.ToList();
            }
        }

        public List<string> ListarCursosDeUsuario(Usuario usuario)
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                var query = from inscripcion in context.Inscripciones
                            join curso in context.Cursos on inscripcion.CodCurso equals curso.CodCurso
                            where inscripcion.Usuario == usuario.Usuario1
                            select curso.NombreCurso;

                return query.ToList();
            }
        }

        public async Task<List<Usuario>> BuscarUsuariosPorNombreAsync(string texto)
        {
            using (var context = new Bu5x9ctsBusinessplusContext())
            {
                string[] textos = texto.Split(" ");
                string textoNombre = "";
                string textoApellido1 = "";
                string textoApellido2 = "";
                for (int i = 0; i < textos.Length; i++)
                {
                    switch (i)
                    {
                        case 0:
                            textoNombre = textos[i];
                            break;
                        case 1:
                            textoApellido1 = textos[i];
                            break;
                        case 2:
                            textoApellido2 = textos[i];
                            break;
                        default:
                            break;
                    }
                }
                var usuariosPorNombreExacto = await context.Usuarios
                                            .Where(u => u.Nombre == textoNombre)
                                            .Select(UsuarioMapping.MapToUsuario(context))
                                            .ToListAsync();

                if (usuariosPorNombreExacto.Count > 0)
                {
                    List<Usuario> usuariosCorrectos = new List<Usuario>();
                    foreach (var usuario in usuariosPorNombreExacto)
                    {
                        string[] cadenaApellidos = usuario.Apellidos.Split(" ");
                        string apellidos = "";
                        for (int i = 0; i < cadenaApellidos.Length; i++)
                        {
                            if (cadenaApellidos[0].ToLower().Equals(textoApellido1.ToLower()))
                            {
                                apellidos = $"{textoApellido1} {textoApellido2}";
                            }
                            else
                            {
                                apellidos = textoApellido1;
                            }
                        }
                        Usuario? usuario2 = await context.Usuarios
                                          .Where(u => u.Nombre == textoNombre &&
                                          u.Apellidos.Contains(apellidos))
                                          .Select(UsuarioMapping.MapToUsuario(context))
                                          .FirstOrDefaultAsync();
                        if (usuario2 != null)
                        {
                            usuariosCorrectos.Add(usuario2);
                        }
                    }
                    return usuariosCorrectos;
                }
                else
                {
                    return await context.Usuarios
                        .Where(u => u.Nombre.Contains(texto))
                        .Select(UsuarioMapping.MapToUsuario(context))
                        .ToListAsync();
                }
            }
        }

        public async Task<Usuario> GetUsuarioAsync(string username)
        {
            return await _context.Usuarios
                .Where(u => u.Usuario1 == username)
                .Select(UsuarioMapping.MapToUsuario(_context))
                .FirstAsync();
        }
    }
}
