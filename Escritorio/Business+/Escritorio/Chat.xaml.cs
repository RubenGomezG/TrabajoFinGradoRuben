using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using BusinessPlusData.ViewModels;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reflection;
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
    /// Lógica de interacción para Chat.xaml
    /// </summary>
    public partial class Chat : Window
    {

        private readonly MensajeRepository _mensajeRepository;
        private readonly ConversacionRepository _conversacionRepository;
        private readonly Bu5x9ctsBusinessplusContext _context;
        public int CodConversacion {  get; set; }
        public int SenderAcademia {  get; set; }
        public ObservableCollection<ChatViewModel> Mensajes { get; set; }

        public Chat()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _mensajeRepository = new MensajeRepository(_context);
            _conversacionRepository = new ConversacionRepository(_context);
            SenderAcademia = 1;
            DataContext = this;
        }

        public void CargarMensajes(int codConversacion)
        {
            CodConversacion = codConversacion;
            Mensajes = _conversacionRepository.ListarMensajeViewModelDeConversacion(codConversacion);
        }

        private async void EnviarMensaje(object sender, RoutedEventArgs e)
        {
            var textoMensaje = MensajeText.Text;
            if (string.IsNullOrWhiteSpace(textoMensaje))
            {
                MessageBox.Show("No puedes enviar un mensaje vacío.");
                return;
            }

            var mensaje = new Mensaje
            {
                CodConversacion = CodConversacion,
                SenderUsername = null,
                SenderCodAcademia = 1,
                Contenido = textoMensaje,
                Timestamp = DateTime.Now
            };
            await _mensajeRepository.CreateMensajeAsync(mensaje);
            await Task.Delay(200);
            var codMensaje = mensaje.CodMensaje;
            var mensajeViewModel = new ChatViewModel
            {
                CodMensaje = codMensaje,
                NombreRemitente = "Yo",
                Contenido = textoMensaje,
                FechaMensaje = DateTime.Now.ToString()
            };

            Mensajes.Add(mensajeViewModel);
            MensajeText.Text = string.Empty;
        }

        private async void EliminarMensaje(object sender, MouseButtonEventArgs e)
        {
            var panel = (StackPanel)sender;
            var codMensaje = (int)panel.Tag;
            var mensajeViewModel = (ChatViewModel)panel.DataContext;
            var mensaje = await _mensajeRepository.ConsultarMensajeAsync(codMensaje);
            if (mensaje.SenderUsername == null)
            {
                var result = MessageBox.Show("¿Estás seguro de que quieres eliminar este mensaje?", "Confirmar eliminación", MessageBoxButton.YesNo);
                if (result == MessageBoxResult.Yes)
                {
                    // Asegúrate de reemplazar 'viewModel' con la instancia de tu ViewModel
                    await _mensajeRepository.DeleteMensajeAsync(codMensaje);
                    Mensajes.Remove(mensajeViewModel);
                }
            }
            else
            {
                MessageBox.Show("Sólo puede borrar mensajes suyos");
            }


        }
    }
}
