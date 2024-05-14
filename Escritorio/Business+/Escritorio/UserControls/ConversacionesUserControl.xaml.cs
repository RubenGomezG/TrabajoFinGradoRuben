using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using Microsoft.VisualBasic;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Escritorio.UserControls
{
    /// <summary>
    /// Lógica de interacción para ConversacionesUserControl.xaml
    /// </summary>
    public partial class ConversacionesUserControl : UserControl, INotifyPropertyChanged
    {
        private ObservableCollection<Conversacione> _conversaciones;
        public ObservableCollection<Conversacione> Conversaciones
        {

            get { return _conversaciones; }
            set
            {
                _conversaciones = value;
                OnPropertyChanged(nameof(Conversaciones));
            }
        }

        private readonly ConversacionRepository _conversacionRepository;
        private readonly UsuarioRepository _usuarioRepository;
        private readonly AcademiaRepository _academiaRepository;
        private readonly MensajeRepository _mensajeRepository;
        private readonly Bu5x9ctsBusinessplusContext _context;

        public ConversacionesUserControl()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _conversacionRepository = new ConversacionRepository(_context);
            _usuarioRepository = new UsuarioRepository(_context);
            _academiaRepository = new AcademiaRepository(_context);
            _mensajeRepository = new MensajeRepository(_context);
            Conversaciones = new ObservableCollection<Conversacione>();
            CargarLista();
        }

        public void CargarLista()
        {
            List<Conversacione> conversaciones = _conversacionRepository.ListarConversacionesDeAcademia();
            foreach (var conversacion in conversaciones)
            {
                Conversaciones.Add(conversacion);
                TarjetaConversacion tarjeta = new TarjetaConversacion();
                tarjeta.BringIntoView();
                /*Usuario usuario = _usuarioRepository.GetUsuarioAsync(conversacion.Usuario2Id);
                Nombre = usuario.Nombre;
                NombreAcademia = _academiaRepository.GetAcademia().Nombre;
                Foto = usuario.ImgPerfil;
                Mensaje = _conversacionRepository.UltimoMensajeDeConversacion(conversacion).Contenido;*/
            }
        }


        private void AbrirChat(object sender, MouseButtonEventArgs e)
        {

        }

        public event PropertyChangedEventHandler? PropertyChanged;
        protected virtual void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
