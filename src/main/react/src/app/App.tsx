import React from 'react';
import './App.css';
import MainPage from "./pages/MainPage";
import {Routes, Route} from 'react-router-dom';
import Navbar from "./template/StandartTemplate/components/Navbar";
import CodelockPage from "./pages/CodelockPage";
import CustomPage from "./pages/CustomPage";
import LogoProject from "./assets/MIDNIGHT.png"
import ProfilePage from "./pages/ProfilePage";
import {PATHS} from "./router/utils/Path.links";
import SettingsPage from "./pages/SettingsPage";

function App() {
      return (
          <div>
              <img style={{display: "block", margin: "auto", padding: '30px 0 10px 0'}} src={LogoProject} alt=""/>
              <Navbar/>
              <Routes>
                  <Route path={PATHS.MAIN} element={<MainPage />} />
                  <Route path={PATHS.CUSTOM} element={<CustomPage />} />
                  <Route path={PATHS.CODELOCK} element={<CodelockPage />} />
                  <Route path={PATHS.PROFILE} element={<ProfilePage />} />
                  <Route path={PATHS.SETTINGS} element={<SettingsPage />} />
              </Routes>
          </div>

  )
}

export default App;
