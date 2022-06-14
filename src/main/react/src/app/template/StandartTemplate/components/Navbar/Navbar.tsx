import React, {useState} from 'react';
import {Link} from 'react-router-dom';
import ButtonBasket from "../Buttons/ButtonBasket";
import './Navbar.scss'
import {dataNavbar} from "../../../../router/utils/Path.navbar";
import Basket from "../Basket";
import {PATHS} from '../../../../router/utils/Path.links';
import settingsIcon from '../../../../assets/settings.png';

let isAdmin = false;
// @ts-ignore
let user;
const Navbar = () => {
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
						{dataNavbar.map((link, idx) =>
							<Link key={idx} className='link' to={link.to}>{link.name}</Link>
						)}
					</li>
				</div>
				<div className='d-flex'>
					<Link className='settingsIconBtn' to={PATHS.SETTINGS}><img className='settingsIcon'
					                                                           src={settingsIcon}
					                                                           alt="settings"/></Link>
					<ButtonBasket onClick={() => setActivePopup(!activePopup)} count={3}/>
					<Basket activePopup={activePopup} setActivePopup={setActivePopup}/>
					{isAuth()
						? <Link style={{textDecoration: 'none'}} className='d-flex' to={PATHS.PROFILE}>
							<div className='profileData'>
								<div className="profileName">
									{
										// @ts-ignore
										user['steamNickName']
									}
								</div>
								<div className="balance">
									{
										// @ts-ignore
										user['balance']
									}
								</div>
							</div>
							<img className='avatar' alt={
									// @ts-ignore
									user['steamNickName']
								}
									src=
								{
									// @ts-ignore
									user['steamAvatarUrl']
								}
							/>
						</Link>
						: <form name="form" action="/login/openid" method="post">
							<div id="openid_input_area">
								<input id="openid_identifier" name="openid_identifier" type="hidden"
								       value="https://steamcommunity.com/openid"/>
								<input id="openid_submit" type="image"
								       src="https://steamcdn-a.akamaihd.net/steamcommunity/public/images/steamworks_docs/russian/sits_small.png"/>
							</div>
						</form>
					}
				</div>
			</div>
		</nav>
	);
};

function isAuth() {
	const xhr = new XMLHttpRequest();

	var result = false
	xhr.open('GET', 'http://alcatraz.dayz-shop.com/user/self', false);
	xhr.onload = function () {
		const status = xhr.status;
		if (status === 200) {
			console.log('Успех:', xhr.responseText);
			try {
				user = JSON.parse(xhr.responseText);
				if (user['id'] !== undefined) {
					result = true
					let rolesArr = user['roles']
					for (const role of rolesArr) {
						if (role['name'] === 'APP_ADMIN' || role['name'] === 'STORE_ADMIN') {
							isAdmin = true
							break
						}
					}
				}
			} catch (e) {
				console.log("fail")
			}
		} else {
			console.log("fail");
		}
	};
	xhr.send();
	return result;
}

export default Navbar;