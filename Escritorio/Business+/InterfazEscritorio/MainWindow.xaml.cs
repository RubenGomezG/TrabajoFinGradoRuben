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

namespace InterfazEscritorio
{
    /// <summary>
    /// Lógica de interacción para MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void Registrarse(object sender, RoutedEventArgs e)
        {
            Registro register = new Registro();
            this.Close();
            register.Show();
        }

        private void Forgot_Password(object sender, RoutedEventArgs e)
        {
            ForgotPassword forgotPassword = new ForgotPassword();
            this.Close();
            forgotPassword.Show();
        }

        private void Login(object sender, RoutedEventArgs e)
        {
            Dashboard dashboard = new Dashboard();
            this.Close();
            dashboard.Show();
        }
    }
}
