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
using System.Net;

namespace Escritorio.UserControls
{
    /// <summary>
    /// Lógica de interacción para CursosUserControl.xaml
    /// </summary>
    public partial class CursosUserControl : UserControl
    {
        private readonly CursoRepository _cursoRepository;
        private readonly InscripcionesRepository _inscripcionesRepository;
        private readonly Bu5x9ctsBusinessplusContext _context;
        public CursosUserControl()
        {
            InitializeComponent();
            _context = new Bu5x9ctsBusinessplusContext();
            _cursoRepository = new CursoRepository(_context);
            _inscripcionesRepository = new InscripcionesRepository(_context);
            CargarDataGrid();

            //Se ocultan las propiedades que enlazan las tablas
            dataGridCursos.AutoGeneratingColumn += (sender, e) =>
            {
                
                if (e.PropertyName == "CodAcademiaNavigation")
                {
                    e.Column.Visibility = Visibility.Collapsed;
                }
                if (e.PropertyName == "Inscripciones")
                {
                    e.Column.Visibility = Visibility.Collapsed;
                }
            };

            //Se descarga la imagen de la academia desde la Web a nuestra carpeta de imágenes, en caso de carga rápida o de recarga de la pestaña, carga la versión local
            //e indicará al usuario que espere unos segundos antes de volver a realizar esa acción.
            try
            {
                using (var client = new WebClient())
                {
                    client.Credentials = new NetworkCredential("admin_images@businesstfg.info", "Clash1ng;");
                    client.DownloadFile($"ftp://81.88.53.129/{App.LoggedAcademia.ImgPerfil}", $"{App.rutaRaiz}\\Escritorio\\Images\\{App.LoggedAcademia.ImgPerfil}");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Se cargará su imagen local. Espere unos segundos para poder actualizarla");
            }

            //Se pinta la imagen desde la url de nuestra aplicación
            BitmapImage img = new BitmapImage(new Uri($"{App.rutaRaiz}\\Escritorio\\Images\\{App.LoggedAcademia.ImgPerfil}"));
            imgCurso.Source = img;
        }

        /*
         * Método que carga la tabla desde la base de datos
         */
        private async void CargarDataGrid() 
        {
            dataGridCursos.ItemsSource = await _cursoRepository.ListarCursosDeAcademia(App.LoggedAcademia);
        }

        /*
         * Método de evento del botón de Crear Curso, valida que todos los campos estén introducidos correctamente.
         */
        private async void CrearCurso(object sender, RoutedEventArgs e)
        {
            if (string.IsNullOrEmpty(txtCodigoCurso.Text))
            {
                if (!string.IsNullOrWhiteSpace(txtNombreCurso.Text) && !string.IsNullOrWhiteSpace(txtPrecio.Text))
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

                                await _cursoRepository.CreateCursoAsync(curso);

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

        /*
         * Método de evento del botón de Editar Curso, valida que todos los campos estén introducidos correctamente.
         */
        private async void EditarCurso(object sender, RoutedEventArgs e)
        {
            Curso curso = await _cursoRepository.GetCursoAsync(int.Parse(txtCodigoCurso.Text));
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
                var resultado = await _cursoRepository.UpdateCursoAsync(curso);

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

        /*
         * Método de evento del botón de Borrar curso, valida que el campo de código de curso no esté vacío o nulo.
         */
        private async void BorrarCurso(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(txtCodigoCurso.Text))
            {
                await _cursoRepository.DeleteCursoAsync(int.Parse(txtCodigoCurso.Text));
                MessageBox.Show($"El curso {txtCodigoCurso.Text} fue eliminado correctamente");
                AbrirDashboard();
            }
            
        }

        /*
         * Método que maneja la carga de datos según el objeto elegido en el dataGridCursos. Asigna a cada elemento de la interfaz sus valores correspondientes
         * y rellena el ListView con los Usuarios inscritos al Curso elegido
         */
        private void ElegirColumna(object sender, SelectionChangedEventArgs e)
        {
            listaClientes.Items.Clear();
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
                foreach (var item in _inscripcionesRepository.ListarUsuariosDeCurso(filaSeleccionada))
                {
                    listaClientes.Items.Add(item);
                }
            }
        }

        /*
         * Método que carga nuevos Cursos en el dataGridCursos según el nombre buscado en el buscador. Se ejecuta cuando cambia el valor del textbox
         */
        private async void BuscarCursos(object sender, TextChangedEventArgs e)
        {
            if (string.IsNullOrEmpty(txtBuscarCursos.Text) || string.IsNullOrWhiteSpace(txtBuscarCursos.Text))
            {
                CargarDataGrid();
            }
            else
            {
                dataGridCursos.ItemsSource = await _cursoRepository.BuscarCursosPorNombreAsync(txtBuscarCursos.Text);
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
