import {PATHS} from "./Path.links";

interface ILink {
    to: string,
    name: string,
}

export const dataNavbar = [
    {
        to: PATHS.MAIN,
        name: 'Товары'
    },
    {
        to: PATHS.CUSTOM,
        name: 'Кастомный сет'
    },
    {
        to: PATHS.CODELOCK,
        name: 'Кодлоки'
    },

] as ILink[]