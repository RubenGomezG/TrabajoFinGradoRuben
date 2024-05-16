using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using Microsoft.EntityFrameworkCore;
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
    /// Lógica de interacción para ForgotPassword.xaml
    /// </summary>
    public partial class ForgotPassword : Window
    {

        private readonly AcademiaRepository _repository;
        private readonly Bu5x9ctsBusinessplusContext _context;
        public ForgotPassword()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _repository = new AcademiaRepository(_context);
        }

        /*
         * Método que actualiza la contraseña de la academia. Recoje el valor de los campos de texto de la vista
         * comprueba que el SHA256 de la contraseña coincide con el guardado en la base de datos y en caso positivo
         * comprueba que el campo de contraseña y el de confirmar sean iguales, en caso positivo, lanza un mensaje
         * que indica al usuario que su contraseña se ha actualizado y vuelve a la pestaña de "Login"
         */
        private async void Cambiar_Contrasena(object sender, RoutedEventArgs e)
        {
            Academia academia = new Academia
            {
                Usuario = username.Text,
                Contrasena = password.Text,
            };
            var resultado = await _repository.ChangePasswordAsync(academia);
            if (resultado != null && password.Text.Equals(confirmarPassword.Text))
            {
                MessageBox.Show("La contraseña fue actualizada correctamente");
                MainWindow mainWindow = new MainWindow();
                this.Close();
                mainWindow.Show();
            }
        }
    }
}
