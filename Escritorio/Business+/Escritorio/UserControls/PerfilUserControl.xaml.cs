using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using Microsoft.Win32;
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
    /// Lógica de interacción para PerfilUserControl.xaml
    /// </summary>
    public partial class PerfilUserControl : UserControl
    {
        private readonly AcademiaRepository _repository;
        private readonly Bu5x9ctsBusinessplusContext _context;
        public PerfilUserControl()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _repository = new AcademiaRepository(_context);
            username.Text = App.LoggedAcademia.Usuario;
            contrasena.Text = App.LoggedAcademia.Contrasena;
            email.Text = App.LoggedAcademia.Email;
            nombre.Text = App.LoggedAcademia.Nombre;
            telefono.Text = App.LoggedAcademia.Telefono.ToString();
            calle.Text = App.LoggedAcademia.Direccion;
            latitud.Text = App.LoggedAcademia.Latitud.ToString();
            longitud.Text = App.LoggedAcademia.Longitud.ToString();
        }
        private void AbrirArchivo_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "Archivos de Imagen|*.jpg;*.jpeg;*.png;*.gif;*.bmp;*.ico|Todos los archivos (*.*)|*.*";

            if (openFileDialog.ShowDialog() == true)
            {
                string rutaArchivo = openFileDialog.FileName;
                // Aquí puedes usar la variable 'rutaArchivo' para trabajar con el archivo seleccionado.
                BitmapImage bitmap = new BitmapImage(new Uri(rutaArchivo));
                imagenSalida.Source = bitmap;
            }

        }

        private void Txt_GotFocus(object sender, RoutedEventArgs e)
        {

            if (sender is TextBox textBox)
            {
                if (textBox.Text == "Dirección")
                {
                    textBox.Text = "";
                }
            }
        }
        private void Txt_LostFocus(object sender, RoutedEventArgs e)
        {

            if (sender is TextBox textBox)
            {
                if (string.IsNullOrWhiteSpace(textBox.Text))
                {
                    if (textBox.Name == "calle")
                    {
                        textBox.Text = "Dirección";
                    }
                }
            }
        }

        private async void Guardar_Click(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrWhiteSpace(username.Text) ||
                string.IsNullOrWhiteSpace(nombre.Text) ||
                string.IsNullOrWhiteSpace(email.Text) ||
                string.IsNullOrWhiteSpace(telefono.Text) ||
                string.IsNullOrWhiteSpace(latitud.Text) ||
                string.IsNullOrWhiteSpace(longitud.Text))
            {
                MessageBox.Show("Los campos Nombre, Apellidos, Email, Teléfono," +
                    "Latitud y Longitud son obligatorios.");
            }
            else if (!int.TryParse(telefono.Text, out int telefonoInt) ||
                    !Double.TryParse(latitud.Text, out double latitudDouble) ||
                    !Double.TryParse(longitud.Text, out double longitudDouble))
            {
                MessageBox.Show("Los campos Telefono, Latitud y Longitud" +
                    "deben ser numéricos");
            }
            else
            {
                try
                {
                    Academia academia = new Academia
                    {
                        Usuario = username.Text,
                        Nombre = nombre.Text,
                        Email = email.Text,
                        Telefono = telefonoInt,
                        Latitud = latitudDouble,
                        Longitud = longitudDouble,
                    };

                    if (imagenSalida.Source != null)
                    {
                        academia.ImgPerfil = username.Text + ".jpg";
                    }
                    if (!string.IsNullOrWhiteSpace(calle.Text))
                    {
                        academia.Direccion = calle.Text;
                    }
                    var resultado = await _repository.EditAcademiaAsync(academia);
                    if (resultado != null)
                    {
                        MessageBox.Show("La academia ha sido actualizada correctamente");
                        App.LoggedAcademia = resultado;
                        AbrirDashboard();
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.StackTrace);
                }
            }
        }

        private void AbrirDashboard()
        {
            Dashboard dashboard = new Dashboard();
            Window.GetWindow(this).Close();
            dashboard.Show();
        }

        private void CerrarSesion(object sender, RoutedEventArgs e)
        {
            MainWindow mainWindow = new MainWindow();
            Window.GetWindow(this).Close();
            mainWindow.Show();
        }
    }
}
