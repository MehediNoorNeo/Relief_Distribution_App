\# 🌍 GPS Guided Relief Distribution System



<p align="center">

&#x20; <strong>An Android application for fair, transparent, and GPS-based disaster relief distribution.</strong>

</p>



<p align="center">

&#x20; <img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge\&logo=android">

&#x20; <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge\&logo=java">

&#x20; <img src="https://img.shields.io/badge/Firebase-Realtime%20Database-FFCA28?style=for-the-badge\&logo=firebase">

&#x20; <img src="https://img.shields.io/badge/Google-Maps%20API-4285F4?style=for-the-badge\&logo=googlemaps">

</p>



\---



\## 📖 Overview



Natural disasters often create significant challenges in distributing relief fairly and efficiently. The \*\*GPS Guided Relief Distribution System\*\* is an Android application designed to improve coordination between relief distributors and disaster-affected people using real-time GPS tracking and location-based services.



The application enables distributors to track each other's locations, locate affected areas, and communicate more effectively while ensuring relief reaches the people who need it most.



\---



\## ✨ Features



\### 👨‍💼 Distributor



\- User Registration \& Login

\- GPS-based Live Location Tracking

\- View Distribution Area on Google Maps

\- Enable/Disable Distribution Mode

\- Emergency Call (999)

\- Manage Relief Distribution

\- Notifications

\- Settings



\### 🧑 Survivor



\- Create Account

\- Request Help

\- View Nearby Relief Points

\- Emergency Call

\- Disaster Information

\- Application Features

\- Settings



\---



\## 🛠️ Technology Stack



| Category | Technology |

|----------|------------|

| Language | Java |

| IDE | Android Studio |

| Database | Firebase Realtime Database |

| Authentication | Firebase Authentication |

| Maps | Google Maps API |

| Location | Fused Location Provider |

| Backend | Firebase |

| UI | XML |



\---



\## 📱 Application Screens



| Splash Screen | Registration |

|:--------------:|:------------:|

| !\[](screenshots/start\_screen.png) | !\[](screenshots/signup.png) |



| Distributor Dashboard | Live Tracking |

|:----------------------:|:-------------:|

| !\[](screenshots/distributor\_dashboard.png) | !\[](screenshots/tracking.png) |



| Survivor Dashboard | Emergency Call |

|:------------------:|:--------------:|

| !\[](screenshots/survivor\_dashboard.png) | !\[](screenshots/emergency\_call.png) |



> \*\*Note:\*\* Place the screenshots inside a `screenshots/` folder and update the filenames if necessary.



\---



\## 📂 Project Structure



```

Relief\_Distribution\_App/

│

├── app/

├── gradle/

├── screenshots/

├── .gitignore

├── build.gradle

├── settings.gradle

└── README.md

```



\---



\## 🚀 Installation



\### Clone the repository



```bash

git clone https://github.com/MehediNoorNeo/Relief\_Distribution\_App.git

```



\### Open the project



\- Open Android Studio

\- Select \*\*Open Existing Project\*\*

\- Choose the cloned folder



\### Configure Firebase



\- Create a Firebase project.

\- Enable \*\*Authentication\*\*.

\- Enable \*\*Realtime Database\*\*.

\- Download \*\*google-services.json\*\*.

\- Place it inside:



```

app/google-services.json

```



\### Configure Google Maps API



Add your Google Maps API key inside:



```xml

<meta-data

&#x20;   android:name="com.google.android.geo.API\_KEY"

&#x20;   android:value="YOUR\_API\_KEY"/>

```



\### Run the application



Connect an Android device or emulator and click \*\*Run\*\*.



\---



\## 🔐 Permissions



The application requires:



\- Internet

\- Fine Location

\- Coarse Location

\- Call Phone

\- Network State



\---



\## 📌 Future Improvements



\- Push Notifications

\- Chat Between Distributor and Survivor

\- Offline Map Support

\- AI-based Relief Demand Prediction

\- Image Upload

\- Route Optimization

\- Disaster Analytics Dashboard



\---



\## 🎯 Research Objective



The goal of this project is to improve disaster response by:



\- Ensuring fair relief distribution

\- Tracking relief distributors in real time

\- Reducing duplicate distribution

\- Helping affected people quickly locate relief points

\- Improving coordination among volunteers



\---



\## 👨‍💻 Developer



\*\*Mehedi Hasan\*\*



\- GitHub: https://github.com/MehediNoorNeo



\---



\## 📄 License



This project is intended for educational and research purposes.

