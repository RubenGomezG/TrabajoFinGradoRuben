using BusinessPlusData.Models;
using BusinessPlusData.Repository;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Escritorio
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private readonly AcademiaRepository _repository;
        private readonly Bu5x9ctsBusinessplusContext _context;
        public MainWindow()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _repository = new AcademiaRepository(_context);
        }
        
        private void Forgot_Password(object sender, RoutedEventArgs e)
        {
            ForgotPassword forgotPassword = new ForgotPassword();
            this.Close();
            forgotPassword.Show();
        }

        /*
         * Método de conexión a la aplicación. Comprueba que los datos de la academia sean correctos,
         * comprobando en el back si la contraseña en texto securizada a SHA256 es la misma que la
         * guardada en la base de datos. En caso positivo, se conecta a la aplicación y guarda el valor
         * de la academia en la variable de aplicación "LoggedAcademia" para utilizar durante el ciclo
         * de vida de la aplicación.
         */
        private async void Login(object sender, RoutedEventArgs e)
        {
            Academia academia = new Academia
            {
                Usuario = username.Text,
                Contrasena = password.Text,
            };
            var resultado = await _repository.LoginAsync(academia);
            if (resultado != null)
            {
                App.LoggedAcademia = resultado;
                System.Diagnostics.Debug.WriteLine("La conexión se realizó correctamente, se ha conectado la Academia");
                Dashboard dashboard = new Dashboard();
                this.Close();
                dashboard.Show();
                
            }
        }
    }
}