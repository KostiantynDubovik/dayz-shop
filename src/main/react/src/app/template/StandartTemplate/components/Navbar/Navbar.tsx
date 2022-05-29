import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import ButtonBasket from "../Buttons/ButtonBasket";
import './Navbar.scss'
import {dataNavbar} from "../../../../router/utils/Path.navbar";
import Basket from "../Basket";
import { PATHS } from '../../../../router/utils/Path.links';
import settingsIcon from '../../../../assets/settings.png';

const Navbar = () => {
    useEffect(() => {
        fetch('http://alcatraz.dayz-shop.com:8081/user/self')
            .then((response) => {
                const json = response.json();
                console.log('Успех:', JSON.stringify(json));
            })

    }, []);

    const isAdmin = true;
    const isAuth = true;
    //const location = useLocation();
    //const pathname = location.pathname;
    const [activePopup, setActivePopup] = useState(false);
    return (
        <nav className="navbar">
            <div className='containerNavbar'>
                <div className='wrapperNavbarLinks'>
                    <div className='logo'>
                        MIDNIGHT DAYZ
                    </div>
                    <li className='navbarLink'>
                        { dataNavbar.map((link , idx)=>
                            <Link key={idx} className='link' to={link.to}>{link.name}</Link>
                        )}
                    </li>
                </div>
                <div className='d-flex'>
                    <Link className='settingsIconBtn' to={PATHS.SETTINGS}><img className='settingsIcon' src={settingsIcon} alt="settings"/></Link>
                    <ButtonBasket onClick={() => setActivePopup(!activePopup)} count={3} />
                    <Basket activePopup={activePopup} setActivePopup={setActivePopup} />
                    { isAuth
                        ? <Link style={{textDecoration: 'none'}} className='d-flex' to={PATHS.PROFILE}>
                                <div className='profileData'>
                                    <div className="profileName">1 из Пердунов</div>
                                    <div className="balance">764 RUB</div>
                                </div>
                                <div className='avatar'>A</div>
                        </Link>
                        : <form name="form" action="http://alcatraz.dayz-shop.com:8081/login/openid" method="post">
                            <div id="openid_input_area">
                                <input id="openid_identifier" name="openid_identifier" type="hidden"
                                       value="https://steamcommunity.com/openid"/>
                                <input id="openid_submit" type="image" src="https://steamcdn-a.akamaihd.net/steamcommunity/public/images/steamworks_docs/russian/sits_small.png" />
                            </div>
                        </form>
                    }
                </div>
            </div>
        </nav>
    );
};

export default Navbar;