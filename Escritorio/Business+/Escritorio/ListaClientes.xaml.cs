using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using BusinessPlusData.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Escritorio
{
    /// <summary>
    /// Lógica de interacción para ListaClientes.xaml
    /// </summary>
    public partial class ListaClientes : Window
    {

        private readonly UsuarioRepository _usuarioRepository;
        private readonly ConversacionRepository _conversacionRepository;
        private readonly Bu5x9ctsBusinessplusContext _context;

        public ListaClientes()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _usuarioRepository = new UsuarioRepository(_context);
            _conversacionRepository = new ConversacionRepository(_context);
            this.DataContext = _usuarioRepository.ListarNombresDeUsuario();
        }

        private async void CrearChat(object sender, MouseButtonEventArgs e)
        {
            var textBlock = (TextBlock)sender;
            var usuarioViewModel = (UsuarioViewModel)textBlock.DataContext;
            var resultado = await _conversacionRepository.GetConversacionByUsernameAsync(usuarioViewModel.Username);
            if (resultado == null)
            {
                var conversacione = new Conversacione
                {
                    Usuario1Id = 1,
                    Usuario2Id = usuarioViewModel.Username,
                };
                _conversacionRepository.CreateConversacion(conversacione);
                var chat = new Chat();
                var codConversacion = conversacione.CodConversacion;
                chat.CargarMensajes(codConversacion);
                chat.Show();
            }
            else
            {
                MessageBox.Show("No puedes crear una conversación ya existente");
            }
        }
    }
}
