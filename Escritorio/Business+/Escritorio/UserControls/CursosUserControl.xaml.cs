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
using System.Globalization;

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
            CargarDataGrid();
            dataGridCursos.AutoGeneratingColumn += (sender, e) =>
            {
                if (e.PropertyName == "CodAcademiaNavigation")
                {
                    e.Column.Visibility = Visibility.Collapsed;
                }
            };
        }

        private async void CargarDataGrid() 
        {
            dataGridCursos.ItemsSource = await _repository.ListarCursosDeAcademia(App.LoggedAcademia);
        }

        private async void CrearCurso(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrEmpty(txtCodigoCurso.Text))
            {
                if (!string.IsNullOrEmpty(txtNombreCurso.Text) &&
                !string.IsNullOrEmpty(txtPrecio.Text) &&
                !string.IsNullOrWhiteSpace(txtNombreCurso.Text) &&
                !string.IsNullOrWhiteSpace(txtPrecio.Text))
                {
                    if (double.TryParse(txtPrecio.Text, out double result))
                    {
                        if (DateTime.TryParseExact(dateFechaInicio.Text, "dd/MM/yyyy", CultureInfo.InvariantCulture, DateTimeStyles.None, out DateTime fechaInicio))
                        {
                            if (DateTime.TryParseExact(dateFechaFin.Text, "dd/MM/yyyy", CultureInfo.InvariantCulture, DateTimeStyles.None, out DateTime fechaFin))
                            {
                                Curso curso = new Curso
                                {
                                    NombreCurso = txtNombreCurso.Text,
                                    Precio = result,
                                    Tipo = comboTipo.Text,
                                    Descripcion = txtDescripcion.Text,
                                    Valoracion = 0.0,
                                    CodAcademia = 1,
                                };
                                curso.FechaInicioCurso = fechaInicio;
                                curso.FechaFinCurso = fechaFin;

                                await _repository.CreateCursoAsync(curso);

                                MessageBox.Show("El curso creado correctamente");
                                AbrirDashboard();
                            }
                            else
                            {
                                MessageBox.Show("Debe elegir una fecha de fin de curso");
                            }
                        }
                        else
                        {
                            MessageBox.Show("Debe elegir una fecha de inicio de curso");
                        }        
                    }
                    else
                    {
                        MessageBox.Show($"{txtPrecio.Text} no es un número válido.");
                    }
                }
                else
                {
                    MessageBox.Show("Debe rellenar todos los campos");
                }
            }
            else
            {
                MessageBox.Show($"Ya existe un curso {txtCodigoCurso.Text}");
                txtCodigoCurso.Text = "";
            }
        }

        private async void EditarCurso(object sender, RoutedEventArgs e)
        {
            Curso curso = await _repository.GetCursoAsync(int.Parse(txtCodigoCurso.Text));
            if (curso != null &&
                !string.IsNullOrEmpty(txtNombreCurso.Text) &&
                !string.IsNullOrEmpty(txtPrecio.Text) &&
                !string.IsNullOrWhiteSpace(txtNombreCurso.Text) &&
                !string.IsNullOrWhiteSpace(txtPrecio.Text) &&
                double.TryParse(txtPrecio.Text, out double result)
                )
            {
                curso.NombreCurso = txtNombreCurso.Text;
                curso.Precio = result;
                if (DateTime.TryParseExact(dateFechaInicio.Text, "dd/MM/yyyy", CultureInfo.InvariantCulture, DateTimeStyles.None, out DateTime fechaInicio))
                {
                    curso.FechaInicioCurso = fechaInicio;
                }
                if (DateTime.TryParseExact(dateFechaFin.Text, "dd/MM/yyyy", CultureInfo.InvariantCulture, DateTimeStyles.None, out DateTime fechaFin))
                {
                    curso.FechaFinCurso = fechaFin;
                }
                curso.Tipo = comboTipo.Text;
                curso.Descripcion = txtDescripcion.Text;
                var resultado = await _repository.UpdateCursoAsync(curso);

                if (resultado != null)
                {
                    MessageBox.Show("El curso fue actualizado correctamente");
                    AbrirDashboard();
                } 
            }
            else 
            {
                MessageBox.Show($"No existe el curso con el valor {txtCodigoCurso.Text}");
            }
        }

        private async void BorrarCurso(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrEmpty(txtCodigoCurso.Text))
            {
                await _repository.DeleteCursoAsync(int.Parse(txtCodigoCurso.Text));
                MessageBox.Show($"El curso {txtCodigoCurso.Text} fue eliminado correctamente");
                AbrirDashboard();
            }
            
        }

        private async void BuscarCursos(object sender, TextChangedEventArgs e)
        {
            if (string.IsNullOrEmpty(txtBuscarCursos.Text) || string.IsNullOrWhiteSpace(txtBuscarCursos.Text))
            {
                CargarDataGrid();
            }
            else
            {
                dataGridCursos.ItemsSource = await _repository.BuscarCursosPorNombreAsync(txtBuscarCursos.Text);
            }
        }

        private void ElegirColumna(object sender, SelectionChangedEventArgs e)
        {
            var filaSeleccionada = dataGridCursos.SelectedItem as Curso;
            if (filaSeleccionada != null)
            {
                txtCodigoCurso.Text = filaSeleccionada.CodCurso.ToString();
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

        private void AbrirDashboard()
        {
            Dashboard dashboard = new Dashboard();
            Window.GetWindow(this).Close();
            dashboard.Show();
        }

        
    }
}
