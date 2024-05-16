using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Policy;
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
using static System.Net.WebRequestMethods;

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

            //Se ocultan las propiedades que enlazan las tablas
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

        /*
         * Método que carga la tabla desde la base de datos
         */
        private async void CargarDataGrid()
        {
            dataGridUsuarios.ItemsSource = await _usuarioRepository.ListarUsuarios();
        }

        /*
         * Método que maneja la carga de datos según el objeto elegido en el dataGridUsuarios. Asigna a cada elemento de la interfaz sus valores correspondientes
         * y rellena el ListView con los Cursos a los que está inscrito el Usuario elegido
         */
        private void ElegirColumna(object sender, SelectionChangedEventArgs e)
        {
            listaCursos.Items.Clear();
            var filaSeleccionada = dataGridUsuarios.SelectedItem as Usuario;
            if (filaSeleccionada != null)
            {
                txtUsername.Text = filaSeleccionada.Usuario1;
                txtNombreUsuario.Text = filaSeleccionada.Nombre;
                txtApellidos.Text = filaSeleccionada.Apellidos;
                txtEmail.Text = filaSeleccionada.Email;
                txtTelefono.Text = filaSeleccionada.Telefono.ToString();
                txtEdad.Text = filaSeleccionada.Edad.ToString();

                //Se descarga la imagen de la academia desde la Web a nuestra carpeta de imágenes, en caso de carga rápida o de recarga de la pestaña, carga la versión local
                //e indicará al usuario que espere unos segundos antes de volver a realizar esa acción.
                try
                {
                    using (var client = new WebClient())
                    {
                        client.Credentials = new NetworkCredential("admin_images@businesstfg.info", "Clash1ng;");
                        client.DownloadFile($"ftp://81.88.53.129/{filaSeleccionada.ImgPerfil}", $"{App.rutaRaiz}\\Escritorio\\Images\\{filaSeleccionada.ImgPerfil}");
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Se cargará su imagen local. Espere unos segundos para poder actualizarla");
                }

                //Se pinta la imagen desde la url de nuestra aplicación
                BitmapImage img = new BitmapImage(new Uri($"{App.rutaRaiz}\\Escritorio\\Images\\{filaSeleccionada.ImgPerfil}"));
                imgCurso.Source = img;

                foreach (var item in _inscripcionesRepository.ListarCursosDeUsuario(filaSeleccionada))
                {
                    listaCursos.Items.Add(item);
                }
            }
        }

        /*
         * Método que carga nuevos Usuarios en el dataGridUsuarios según el nombre y apellidos buscado en el buscador. Se ejecuta cuando cambia el valor del textbox
         */
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
