import React, { createContext, useState } from 'react';

export const UserContext = createContext(undefined);

export function UserProvider({ children }) {
    const [userId, setUserId] = useState(undefined);

    return (
        <UserContext.Provider value={{ userId, setUserId }}>
            {children}
        </UserContext.Provider>
    );
}