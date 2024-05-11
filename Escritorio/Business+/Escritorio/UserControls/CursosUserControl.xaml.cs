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
using System.ComponentModel;

namespace Escritorio.UserControls
{
    /// <summary>
    /// Lógica de interacción para CursosUserControl.xaml
    /// </summary>
    public partial class CursosUserControl : UserControl
    {
        private readonly CursoRepository _repository;
        private readonly Bu5x9ctsBusinessplusContext _context;
        public CursosUserControl()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _repository = new CursoRepository(_context);
            dataGridCursos.ItemsSource = _repository.ListarCursosDeAcademia(App.LoggedAcademia);
            dataGridCursos.AutoGeneratingColumn += (sender, e) =>
            {
                if (e.PropertyName == "CodAcademiaNavigation")
                {
                    e.Column.Visibility = Visibility.Collapsed;
                }
            };
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

        private void ElegirColumna(object sender, SelectionChangedEventArgs e)
        {
            var filaSeleccionada = dataGridCursos.SelectedItem as Curso;
            if (filaSeleccionada != null)
            {
                txtNombreCurso.Text = filaSeleccionada.NombreCurso;
                txtPrecio.Text = filaSeleccionada.Precio.ToString();
                dateFechaInicio.Text = filaSeleccionada.FechaInicioCurso.ToString();
                dateFechaFin.Text = filaSeleccionada.FechaFinCurso.ToString();

                var tipoEnLista = comboTipo.Items.OfType<ComboBoxItem>().FirstOrDefault(item => item.Content.ToString() == filaSeleccionada.Tipo);
                if (tipoEnLista != null)
                {
                    comboTipo.SelectedItem = tipoEnLista;
                }

                txtDescripcion.Text = filaSeleccionada.Descripcion;
                //TODO imagenes y listview de Usuarios inscritos
            }
        }
    }
}
