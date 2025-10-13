import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    // Check if user is logged in on app start
    useEffect(() => {
        checkAuthStatus();
    }, []);

    const checkAuthStatus = async () => {
        try {
            console.log('Checking auth status...');
            const response = await fetch('http://localhost:8080/api/auth/validate-session', {
                method: 'GET',
                credentials: 'include',
            });

            console.log('Validation response status:', response.status);
            console.log('Validation response headers:', response.headers);
            console.log('Response ok?', response.ok);

            if (response.ok) {
                const result = await response.json();
                console.log('Validation result:', result);
                console.log('Result success?', result.success);
                console.log('Result data:', {
                    userId: result.userId,
                    username: result.username,
                    userType: result.userType
                });

                if (result.success) {
                    // Backend is now returning fields correctly
                    const userObj = {
                        id: result.userId,
                        username: result.username,  // Backend correctly returns username
                        userType: result.userType,  // Backend correctly returns userType  
                        isAuthenticated: true
                    };
                    console.log('Setting user object:', userObj);
                    setUser(userObj);
                    console.log('User authenticated successfully');
                } else {
                    console.log('Validation failed:', result.message);
                    setUser(null); // Clear user state when validation fails
                }
            } else {
                console.log('Validation response not ok:', response.status, response.statusText);
                setUser(null); // Clear user state when response is not ok
            }
        } catch (error) {
            console.error('Session validation error:', error);
            setUser(null); // Clear user state on error
        }
        setIsLoading(false);
    };


    const login = async (userType, credentials) => {
        setIsLoading(true);
        try {
            const endpoint = `/api/auth/${userType.toLowerCase()}`;
            const response = await fetch(`http://localhost:8080${endpoint}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include', // Important for cookies
                body: JSON.stringify(credentials),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Login failed');
            }

            const result = await response.json();

            if (result.success) {
                setUser({
                    id: result.userId,
                    username: result.username,
                    userType: result.userType,
                    isAuthenticated: true
                });
                return { success: true, userType: result.userType };
            } else {
                throw new Error(result.message || 'Login failed');
            }
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        } finally {
            setIsLoading(false);
        }
    };

    const logout = async () => {
        try {
            await fetch('http://localhost:8080/api/auth/logout', {
                method: 'POST',
                credentials: 'include',
            });

            setUser(null);

            // Clear cookie manually as backup
            document.cookie = 'userSession=; path=/; expires=Thu, 01 Jan 1970 00:00:01 GMT;';

            return { success: true };
        } catch (error) {
            console.error('Logout error:', error);
            // Still clear local state even if API call fails
            setUser(null);
            document.cookie = 'userSession=; path=/; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
            return { success: false, error: error.message };
        }
    };

    const register = async (patientData) => {
        setIsLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(patientData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Registration failed');
            }

            const result = await response.json();
            return { success: true, message: result.message };
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        } finally {
            setIsLoading(false);
        }
    };

    const value = {
        user,
        isLoading,
        login,
        logout,
        register,
        checkAuthStatus,
        isAuthenticated: !!user?.isAuthenticated,
        userType: user?.userType || null,
        username: user?.username || null
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;