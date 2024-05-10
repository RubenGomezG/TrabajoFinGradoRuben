using BusinessPlusData.Models;
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
    /// Lógica de interacción para Dashboard.xaml
    /// </summary>
    public partial class Dashboard : Window
    {
        private readonly Bu5x9ctsBusinessplusContext _context;

        public Dashboard()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
        }
    }
}
