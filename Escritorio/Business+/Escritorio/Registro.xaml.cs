using BusinessPlusData.Models;
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
using System.Windows.Shapes;

namespace Escritorio
{
    /// <summary>
    /// Lógica de interacción para Registro.xaml
    /// </summary>
    public partial class Registro : Window
    {
        internal class User
        {
            public string nombre { get; set; }
            public string apellidos { get; set; }
            public string email { get; set; }
            public long telefono { get; set; }
        }

        public Registro()
        {
            InitializeComponent();
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
                if (textBox.Text == "Dirección" || textBox.Text == "Ciudad" || textBox.Text == "Provincia" || textBox.Text == "Código Postal" || textBox.Text == "País")
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
                    else if (textBox.Name == "ciudad")
                    {
                        textBox.Text = "Ciudad";
                    }
                    else if (textBox.Name == "provincia")
                    {
                        textBox.Text = "Provincia";
                    }
                    else if (textBox.Name == "codigoPostal")
                    {
                        textBox.Text = "Código Postal";
                    }
                    else if (textBox.Name == "pais")
                    {
                        textBox.Text = "País";
                    }
                }
            }
        }

        private void Cancelar_Click(object sender, RoutedEventArgs e)
        {
            Window mainwindow = new MainWindow();
            this.Close();
            mainwindow.Show();
        }

        private void Guardar_Click(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrWhiteSpace(nombre.Text) || string.IsNullOrWhiteSpace(apellidos.Text) || string.IsNullOrWhiteSpace(email.Text) || string.IsNullOrWhiteSpace(telefono.Text))
            {

                MessageBox.Show("Los campos Nombre, Apellidos, Email y Teléfono son obligatorios.");
                Window mainwindow = new MainWindow();
                this.Close();
                mainwindow.Show();
            }
            else
            {
                try
                {
                    List<User> users = new List<User>();
                    users.Add(new User() { nombre = nombre.Text, apellidos = apellidos.Text, email = apellidos.Text, telefono = int.Parse(telefono.Text) });
                    datosEmpleados.ItemsSource = users;
                    new Usuario { }
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.StackTrace);
                }
            }
        }
    }
}
