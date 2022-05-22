import React from "react";
import {PATHS} from "./utils/Path.links";
import MainPage from "../pages/MainPage";
import CustomPage from "../pages/CustomPage";
import CodelockPage from "../pages/CodelockPage";

export interface IRoute {
    path: string;
    component: React.ComponentType;
    exact?: boolean;
    withoutAuth?: boolean,
    childRoutes?: IRoute[]
}

export const Routes: IRoute[] = [
    { path: PATHS.MAIN, component: MainPage, withoutAuth: true },
    { path: PATHS.CUSTOM, component: CustomPage },
    { path: PATHS.CODELOCK, component: CodelockPage, withoutAuth: true },
    { path: PATHS.CODELOCK, component: CodelockPage, withoutAuth: true },
    { path: PATHS.CODELOCK, component: CodelockPage, withoutAuth: true },
];
