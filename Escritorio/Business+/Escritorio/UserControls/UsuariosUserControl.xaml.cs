using BusinessPlusData.Models;
using BusinessPlusData.Repository;
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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Escritorio.UserControls
{
    /// <summary>
    /// Lógica de interacción para UsuariosUserControl.xaml
    /// </summary>
    public partial class UsuariosUserControl : UserControl
    {
        private readonly UsuarioRepository _usuarioRepository;
        private readonly InscripcionesRepository _inscripcionesRepository;

        private readonly Bu5x9ctsBusinessplusContext _context;
        public UsuariosUserControl()
        {
            InitializeComponent();

            _context = new Bu5x9ctsBusinessplusContext();
            _usuarioRepository = new UsuarioRepository(_context);
            _inscripcionesRepository = new InscripcionesRepository(_context);
            CargarDataGrid();

            dataGridUsuarios.AutoGeneratingColumn += (sender, e) =>
            {
                if (e.PropertyName == "ConversacioneUsuario2s")
                {
                    e.Column.Visibility = Visibility.Collapsed;
                }
                if (e.PropertyName == "Mensajes")
                {
                    e.Column.Visibility = Visibility.Collapsed;
                }
                if (e.PropertyName == "Inscripciones")
                {
                    e.Column.Visibility = Visibility.Collapsed;
                }
            };
        }

        private async void CargarDataGrid()
        {
            dataGridUsuarios.ItemsSource = await _usuarioRepository.ListarUsuarios();
        }

        private void ElegirColumna(object sender, SelectionChangedEventArgs e)
        {
            listaClientes.Items.Clear();
            var filaSeleccionada = dataGridUsuarios.SelectedItem as Usuario;
            if (filaSeleccionada != null)
            {
                txtUsername.Text = filaSeleccionada.Usuario1.ToString();
                txtNombreUsuario.Text = filaSeleccionada.Nombre;
                txtApellidos.Text = filaSeleccionada.Apellidos;
                txtEmail.Text = filaSeleccionada.Email;
                txtTelefono.Text = filaSeleccionada.Telefono.ToString();
                txtEdad.Text = filaSeleccionada.Edad.ToString();
                //TODO imagenes
                foreach (var item in _inscripcionesRepository.ListarCursosDeUsuario(filaSeleccionada))
                {
                    listaClientes.Items.Add(item);
                }
            }
        }

        private async void BuscarUsuarios(object sender, TextChangedEventArgs e)
        {
            if (string.IsNullOrEmpty(txtBuscarUsuarios.Text) || string.IsNullOrWhiteSpace(txtBuscarUsuarios.Text))
            {
                CargarDataGrid();
            }
            else
            {
                dataGridUsuarios.ItemsSource = await _usuarioRepository.BuscarUsuariosPorNombreAsync(txtBuscarUsuarios.Text);
            }
        }
    }
}
