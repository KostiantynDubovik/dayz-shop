import { useCallback, useEffect, useState } from "react";
import { useLocation } from "react-router";

interface IUseVisible {
    initialIsVisible: boolean,
    ref: any,
    provocation?: string,
}

export const useVisible = ({initialIsVisible, ref, provocation}: IUseVisible) => {
    const [isVisible, setIsVisible] = useState(initialIsVisible);
    const { pathname } = useLocation();

    const handleClickOutside = useCallback((event: any) => {
        if ((ref.current && !ref.current.contains(event.target))
            || (provocation && event.target.classList.contains(provocation))
        ) {
            setIsVisible(false);
        }
    }, [setIsVisible, provocation, ref]);

    useEffect(() => {
        document.addEventListener('click', handleClickOutside, true);
        return () => {
            document.removeEventListener('click', handleClickOutside, true);
        };
    }, [handleClickOutside]);

    useEffect(() => {
        setIsVisible(false);
    }, [pathname])

    return {ref, isVisible, setIsVisible};
}
