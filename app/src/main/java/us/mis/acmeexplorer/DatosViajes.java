package us.mis.acmeexplorer;

import java.util.List;

import us.mis.acmeexplorer.Viaje;

public class DatosViajes {

    public final static String[] salidas={"Madrid","Barcelona","Sevilla","Málaga","Valencia","Palma de Mayorca","Gran Canaria"};

    public final static String[] destinos={"Ámsterdam, Holanda","Beijing, China","Beirut, Líbano","Bergen, Noruega","Brujas, Bélgica","Budapest, Hungría","Buenos Aires, Argentina",
            "Ciudad del Cabo, Sudáfrica","Cartagena, Colombia","Chefchaouen, Marruecos","Chiang Mai, Tailandia","Copenhague, Dinamarca","Cuzco, Perú","Doha, Qatar","Dubrovnik, Croacia",
            "Edimburgo, Escocia","Florencia, Italia", "La Habana, Cuba","Hanoi, Vietnam","Hong Kong, China","Estambul, Turquía","Jaipur, India","Jerusalén, Israel","Kioto, Japón",
            "Liubliana, Eslovenia","Londres, Reino Unido","Luang Prabang, Laos","Mascate, Omán","Nueva York, Nueva York","París, Francia","Oporto, Portugal","Praga, República Checa",
            "Ciudad de Quebec, Canadá","Queenstown, Nueva Zelanda","Quito, Ecuador","Río de Janeiro, Brasil","Roma, Italia","San Petersburgo, Rusia","San Francisco, California",
            "San Miguel, México","Seúl, Corea del Sur","Singapur, Singapur","Sídney, Australia","Tallin, Estonia","La Valeta, Malta","Venecia, Italia","Viena, Austria",
            "Zúrich, Suiza"};

    public final static Double[] latitudes={52.3740300, 39.9075, 33.89332, 60.39299, 51.20892, 47.49835, -34.61315,
                    -33.92584, 10.39972, 35.16878, 18.79038, 55.67594, -13.52264, 25.28545, 42.64125,
                    55.95206, 43.77925, 23.13302, 21.0245, 22.27832, 41.01384, 26.91962, 31.76904, 35.02107,
                    46.05108, 51.51279, 19.88601, 23.58413, 40.71427, 48.85341, 41.14961, 50.08804,
                    46.81228, -45.03023, -0.22985, -22.90642, 41.89193, 59.93863, 37.77493,
                    18.99105, 37.566, 1.28967, -33.86785, 59.43696, 35.89968, 45.43713, 48.20849,
                    47.36667};
    public final static Double[] longitudes={4.8896900, 116.39723, 35.50157, 5.32415, 3.22424, 19.04045, -58.37723,
                    18.42322, -75.51444, -5.2636, 98.98468, 12.56553, -71.96734, 51.53096, 18.10909,
                    -3.19648, 11.24626, -82.38304, 105.84117, 114.17469, 28.94966, 75.78781, 35.21633, 135.75385,
                    14.50513, -0.09184, 102.13503, 58.40778, -74.00597, 2.3488, -8.61099, 14.42076,
                    -71.21454, 168.66271, -78.52495, -43.18223, 12.51133, 30.31413, -122.41942,
                    -99.66555, 126.9784, 103.85007, 151.20732, 24.75353, 14.5148, 12.33265, 16.37208,
                    8.55};

    public final static String[] urlFotos={"https://media.traveler.es/photos/61375f04ea50dbd37eade9a7/master/w_2580%2Cc_limit/213200.jpg",
            "https://media.traveler.es/photos/61375f03ea50dbd37eade9a3/master/w_2580%2Cc_limit/213176.jpg",
            "https://media.traveler.es/photos/61375f0332d932c80fcb80f5/master/w_2580%2Cc_limit/111206.jpg",
            "https://media.traveler.es/photos/61375f04f00fb1ba8d86679e/master/w_2580%2Cc_limit/213177.jpg",
            "https://media.traveler.es/photos/61375f03d7c7024f9175dbef/master/w_2580%2Cc_limit/213211.jpg",
            "https://media.traveler.es/photos/61375f04ea50dbd37eade9a6/master/w_800%2Cc_limit/213213.jpg",
            "https://media.traveler.es/photos/61375f0472cad4b2dbd5d0d2/master/w_800%2Cc_limit/213180.jpg",
            "https://media.traveler.es/photos/61375f04c4f3d957866678a5/master/w_800%2Cc_limit/213190.jpg",
            "https://media.traveler.es/photos/61375f04c4f3d957866678a8/master/w_800%2Cc_limit/213216.jpg",
            "https://media.traveler.es/photos/61375f04030de94d067fbfd0/master/w_800%2Cc_limit/213219.jpg",
            "https://media.traveler.es/photos/61375f0432d932c80fcb80f9/master/w_800%2Cc_limit/213175.jpg",
            "https://media.traveler.es/photos/61375f04f00fb1ba8d8667a4/master/w_800%2Cc_limit/213184.jpg",
            "https://media.traveler.es/photos/61375f0432d932c80fcb80fb/master/w_800%2Cc_limit/120895.jpg",
            "https://media.traveler.es/photos/61375f04c4f3d957866678a9/master/w_800%2Cc_limit/213179.jpg",
            "https://media.traveler.es/photos/61375f05ba2a75fcba4bdd3d/master/w_800%2Cc_limit/213187.jpg",
            "https://media.traveler.es/photos/61375f05ea50dbd37eade9ad/master/w_800%2Cc_limit/213226.jpg",
            "https://media.traveler.es/photos/61375f05f00fb1ba8d8667a6/master/w_800%2Cc_limit/213189.jpg",
            "https://media.traveler.es/photos/61375f05f130191a954c6b93/master/w_800%2Cc_limit/213227.jpg",
            "https://media.traveler.es/photos/61375f05ba2a75fcba4bdd40/master/w_800%2Cc_limit/213174.jpg",
            "https://media.traveler.es/photos/61375f05f130191a954c6b94/master/w_800%2Cc_limit/213178.jpg",
            "https://media.traveler.es/photos/61375f05c4f3d957866678ab/master/w_800%2Cc_limit/213228.jpg",
            "https://media.traveler.es/photos/61375f05ba2a75fcba4bdd42/master/w_800%2Cc_limit/213229.jpg",
            "https://media.traveler.es/photos/61375f05c4f3d957866678ad/master/w_800%2Cc_limit/213185.jpg",
            "https://media.traveler.es/photos/61375f05f00fb1ba8d8667a9/master/w_800%2Cc_limit/213231.jpg",
            "https://media.traveler.es/photos/61375f05d7c7024f9175dbf5/master/w_800%2Cc_limit/213232.jpg",
            "https://media.traveler.es/photos/61375f06bae07f0d8a492432/master/w_800%2Cc_limit/213230.jpg",
            "https://media.traveler.es/photos/61375f06bae07f0d8a492431/master/w_800%2Cc_limit/213188.jpg",
            "https://media.traveler.es/photos/61375f06c4f3d957866678af/master/w_800%2Cc_limit/140338.jpg",
            "https://media.traveler.es/photos/61375f064c612f07ec3984de/master/w_800%2Cc_limit/213191.jpg",
            "https://media.traveler.es/photos/61375f06bae07f0d8a492434/master/w_800%2Cc_limit/213234.jpg",
            "https://media.traveler.es/photos/61375f06d7c7024f9175dbf7/master/w_800%2Cc_limit/213235.jpg",
            "https://media.traveler.es/photos/61375f06c4f3d957866678b5/master/w_800%2Cc_limit/213236.jpg",
            "https://media.traveler.es/photos/61375f06c4f3d957866678b4/master/w_800%2Cc_limit/213181.jpg",
            "https://media.traveler.es/photos/61375f0672cad4b2dbd5d0d8/master/w_800%2Cc_limit/213238.jpg",
            "https://media.traveler.es/photos/61375f06030de94d067fbfd5/master/w_800%2Cc_limit/213239.jpg",
            "https://media.traveler.es/photos/61375f06ea50dbd37eade9b6/master/w_800%2Cc_limit/213240.jpg",
            "https://media.traveler.es/photos/61375f06d7c7024f9175dbfa/master/w_800%2Cc_limit/213186.jpg",
            "https://media.traveler.es/photos/61375f06f00fb1ba8d8667ad/master/w_800%2Cc_limit/213192.jpg",
            "https://media.traveler.es/photos/61375f06ba2a75fcba4bdd45/master/w_800%2Cc_limit/210847.jpg",
            "https://media.traveler.es/photos/61375f07f130191a954c6b96/master/w_800%2Cc_limit/213241.jpg",
            "https://media.traveler.es/photos/61375f0786b46eac7cf5940c/master/w_800%2Cc_limit/213173.jpg",
            "https://media.traveler.es/photos/61375f074c612f07ec3984e2/master/w_800%2Cc_limit/213243.jpg",
            "https://media.traveler.es/photos/61375f0732d932c80fcb8103/master/w_800%2Cc_limit/213244.jpg",
            "https://media.traveler.es/photos/61375f0732d932c80fcb8105/master/w_800%2Cc_limit/213182.jpg",
            "https://media.traveler.es/photos/61375f07f00fb1ba8d8667b1/master/w_800%2Cc_limit/213193.jpg",
            "https://media.traveler.es/photos/61375f07bae07f0d8a492436/master/w_800%2Cc_limit/213183.jpg",
            "https://media.traveler.es/photos/61375f0786b46eac7cf5940f/master/w_800%2Cc_limit/213245.jpg",
            "https://media.traveler.es/photos/61375f07f130191a954c6b98/master/w_800%2Cc_limit/213246.jpg"
    };

    public final static String[] incluido = {"Acceso a piscina", "Detalle de bienvenida", "Traslados", "Circuitos turísticos",
            "Excursiones", "Seguro de viaje", "Tasas locales", "Espectáculos", "Eventos culturales", "Paquete de actividades",
            "Degustación", "Habitación con vistas", "Guía turístico", "Libro de recomendaciones"};

    public final static String[] regimen = {"Desayuno incluido", "Media pensión", "Pensión completa"};

    public static List<Viaje> VIAJES = null;

    public static final String intentViaje ="Viaje_intent" ;
    public final static String filtro = "filtro";
    public final static String inicio = "inicio";
    public final static String fin = "fin";
    public final static String precioMax = "precioMax";
    public final static String precioMin = "precioMin";
    public final static String USUARIO = "usuario";
}

