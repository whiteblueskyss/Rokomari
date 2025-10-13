import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { LoadingSpinner } from './common';

const ProtectedRoute = ({ children, allowedRoles = [] }) => {
    const { user, isLoading } = useAuth();

    console.log('ProtectedRoute - Auth state:', {
        isLoading,
        user: user ? {
            id: user.id,
            username: user.username,
            userType: user.userType,
            isAuthenticated: user.isAuthenticated
        } : null,
        allowedRoles
    });

    // Show loading spinner while checking authentication
    if (isLoading) {
        return <LoadingSpinner text="Checking authentication..." />;
    }

    // If not authenticated, redirect to login
    if (!user || !user.isAuthenticated) {
        console.log('ProtectedRoute: User not authenticated, redirecting to login');
        return <Navigate to="/" replace />;
    }

    // If specific roles are required, check user role
    if (allowedRoles.length > 0 && !allowedRoles.includes(user.userType)) {
        console.log('ProtectedRoute: User role not allowed', user.userType, 'allowed:', allowedRoles);
        return <Navigate to="/" replace />;
    }

    console.log('ProtectedRoute: Access granted');
    // User is authenticated and authorized, render the children
    return children;
};

export default ProtectedRoute;