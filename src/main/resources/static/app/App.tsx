import React from 'react';
import './App.css';
import MainPage from "./pages/MainPage";
import {Routes, Route} from 'react-router-dom';
import Navbar from "./template/StandartTemplate/components/Navbar";
import CodelockPage from "./pages/CodelockPage";
import CustomPage from "./pages/CustomPage";
import {Navigation} from "@mui/icons-material";
import LogoProject from "./assets/MIDNIGHT.png"
import ProfilePage from "./pages/ProfilePage";
import Popup from "./shared/Popup";


function App() {
      return (
          <div>
              <img style={{display: "block", margin: "auto", padding: '30px 0 10px 0'}} src={LogoProject} alt=""/>
              <Navbar/>
              <Routes>
                  <Route path={'/'} element={<MainPage />} />
                  <Route path={'/custom'} element={<CustomPage />} />
                  <Route path={'/codelock'} element={<CodelockPage />} />
                  <Route path={'/profile'} element={<ProfilePage />} />


              </Routes>
          </div>

  )
}

export default App;
