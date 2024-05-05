using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace InterfazEscritorio
{
    public class MainViewModel : INotifyPropertyChanged
    {
        private ObservableCollection<string> _yourCollection;
        public ObservableCollection<string> YourCollection
        {
            get { return _yourCollection; }
            set
            {
                _yourCollection = value;
                OnPropertyChanged(nameof(YourCollection));
            }
        }

        public MainViewModel()
        {
            YourCollection = new ObservableCollection<string>();
            // Aquí puedes inicializar y agregar elementos a tu colección si es necesario
            YourCollection.Add("Elemento 1");
            YourCollection.Add("Elemento 2");
            YourCollection.Add("Elemento 3");
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
