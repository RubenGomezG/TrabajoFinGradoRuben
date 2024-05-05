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

namespace InterfazEscritorio.UserControls
{
    /// <summary>
    /// Lógica de interacción para CursosUserControl.xaml
    /// </summary>
    public partial class CursosUserControl : UserControl
    {
        public CursosUserControl()
        {
            InitializeComponent();
            DataContext = new MainViewModel();
        }

        private void CrearCurso(object sender, RoutedEventArgs e)
        {

        }

        private void EditarCurso(object sender, RoutedEventArgs e)
        {

        }

        private void BorrarCurso(object sender, RoutedEventArgs e)
        {

        }
    }
}
