import React from 'react';

/**
 * Reusable Stats Card Component
 */
export const StatsCard = ({ title, value, icon, color = 'blue', loading = false }) => {
    const colorClasses = {
        blue: { bg: 'bg-blue-100', text: 'text-blue-600' },
        green: { bg: 'bg-green-100', text: 'text-green-600' },
        purple: { bg: 'bg-purple-100', text: 'text-purple-600' },
        yellow: { bg: 'bg-yellow-100', text: 'text-yellow-600' },
        red: { bg: 'bg-red-100', text: 'text-red-600' }
    };

    const colors = colorClasses[color] || colorClasses.blue;

    return (
        <div className="bg-white p-6 rounded-lg shadow-sm border">
            <div className="flex items-center">
                <div className={`p-2 ${colors.bg} rounded-lg`}>
                    <div className={`w-6 h-6 ${colors.text}`}>
                        {icon}
                    </div>
                </div>
                <div className="ml-4">
                    <p className="text-sm font-medium text-gray-600">{title}</p>
                    <p className="text-2xl font-bold text-gray-900">
                        {loading ? '...' : value}
                    </p>
                </div>
            </div>
        </div>
    );
};

/**
 * Reusable Dashboard Header Component
 */
export const DashboardHeader = ({
    title,
    subtitle,
    userType,
    userName,
    specialization,
    onRefresh,
    onLogout,
    loading = false
}) => (
    <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center py-4">
                <div>
                    <h1 className="text-2xl font-bold text-gray-900">{title}</h1>
                    <p className="text-sm text-gray-600">{subtitle}</p>
                    {specialization && (
                        <p className="text-xs text-gray-500">{specialization}</p>
                    )}
                </div>
                <div className="flex items-center space-x-4">
                    <span className="text-gray-700">Welcome, {userName}</span>
                    <button
                        onClick={onRefresh}
                        disabled={loading}
                        className={`px-4 py-2 rounded-lg text-sm font-medium ${loading
                                ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                                : 'bg-blue-600 text-white hover:bg-blue-700'
                            }`}
                    >
                        {loading ? 'Loading...' : 'Refresh'}
                    </button>
                    <button
                        onClick={onLogout}
                        className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                    >
                        Logout
                    </button>
                </div>
            </div>
        </div>
    </header>
);

/**
 * Reusable Loading Component
 */
export const LoadingSpinner = ({ message = 'Loading...', size = 'md' }) => {
    const sizeClasses = {
        sm: 'h-4 w-4',
        md: 'h-8 w-8',
        lg: 'h-12 w-12'
    };

    return (
        <div className="text-center py-8">
            <div className={`animate-spin rounded-full border-b-2 border-blue-600 mx-auto ${sizeClasses[size]}`}></div>
            <p className="mt-2 text-gray-600">{message}</p>
        </div>
    );
};

/**
 * Reusable Error Component
 */
export const ErrorMessage = ({ message, onRetry }) => (
    <div className="text-center py-8">
        <div className="text-red-600 mb-4">
            <svg className="w-12 h-12 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
        </div>
        <p className="text-gray-600 mb-4">{message}</p>
        {onRetry && (
            <button
                onClick={onRetry}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
            >
                Try Again
            </button>
        )}
    </div>
);

/**
 * Reusable Empty State Component
 */
export const EmptyState = ({ message, actionText, onAction }) => (
    <div className="text-center py-8">
        <p className="text-gray-500 mb-4">{message}</p>
        {actionText && onAction && (
            <button
                onClick={onAction}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
            >
                {actionText}
            </button>
        )}
    </div>
);