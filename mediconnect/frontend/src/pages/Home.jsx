import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
            <div className="container mx-auto px-4 py-16">
                <div className="text-center">
                    <h1 className="text-4xl font-bold text-gray-900 mb-4">
                        Welcome to MediConnect
                    </h1>
                    <p className="text-xl text-gray-600 mb-12">
                        Your Digital Medical Appointment System
                    </p>

                    <div className="grid md:grid-cols-3 gap-8 max-w-4xl mx-auto">
                        {/* Patient Section */}
                        <div className="bg-white rounded-lg shadow-lg p-8">
                            <div className="text-blue-500 mb-4">
                                <svg className="w-16 h-16 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clipRule="evenodd" />
                                </svg>
                            </div>
                            <h2 className="text-2xl font-semibold text-gray-900 mb-4">Patients</h2>
                            <p className="text-gray-600 mb-6">
                                Book appointments with doctors, view medical history, and manage your health records.
                            </p>
                            <div className="space-y-3">
                                <Link
                                    to="/register"
                                    className="block w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition duration-200"
                                >
                                    Register as Patient
                                </Link>
                                <Link
                                    to="/login"
                                    className="block w-full border border-blue-600 text-blue-600 py-2 px-4 rounded-md hover:bg-blue-50 transition duration-200"
                                >
                                    Patient Login
                                </Link>
                            </div>
                        </div>

                        {/* Doctor Section */}
                        <div className="bg-white rounded-lg shadow-lg p-8">
                            <div className="text-green-500 mb-4">
                                <svg className="w-16 h-16 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                                    <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                            <h2 className="text-2xl font-semibold text-gray-900 mb-4">Doctors</h2>
                            <p className="text-gray-600 mb-6">
                                Manage appointments, view patient records, and write prescriptions.
                            </p>
                            <div className="space-y-3">
                                <Link
                                    to="/login"
                                    className="block w-full bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 transition duration-200"
                                >
                                    Doctor Login
                                </Link>
                            </div>
                        </div>

                        {/* Admin Section */}
                        <div className="bg-white rounded-lg shadow-lg p-8">
                            <div className="text-purple-500 mb-4">
                                <svg className="w-16 h-16 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-6-3a2 2 0 11-4 0 2 2 0 014 0zm-2 4a5 5 0 00-4.546 2.916A5.986 5.986 0 0010 16a5.986 5.986 0 004.546-2.084A5 5 0 0010 11z" clipRule="evenodd" />
                                </svg>
                            </div>
                            <h2 className="text-2xl font-semibold text-gray-900 mb-4">Administrator</h2>
                            <p className="text-gray-600 mb-6">
                                Complete system management including doctors, patients, and appointments.
                            </p>
                            <div className="space-y-3">
                                <Link
                                    to="/login"
                                    className="block w-full bg-purple-600 text-white py-2 px-4 rounded-md hover:bg-purple-700 transition duration-200"
                                >
                                    Admin Login
                                </Link>
                            </div>
                        </div>
                    </div>

                    <div className="mt-16 text-center">
                        <h3 className="text-2xl font-semibold text-gray-900 mb-4">Features</h3>
                        <div className="grid md:grid-cols-4 gap-6 max-w-4xl mx-auto">
                            <div className="text-center">
                                <div className="text-blue-500 mb-2">
                                    <svg className="w-8 h-8 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clipRule="evenodd" />
                                    </svg>
                                </div>
                                <h4 className="font-medium text-gray-900">Appointment Booking</h4>
                                <p className="text-sm text-gray-600">Easy scheduling system</p>
                            </div>

                            <div className="text-center">
                                <div className="text-green-500 mb-2">
                                    <svg className="w-8 h-8 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M3 4a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H4a1 1 0 01-1-1V4zM3 10a1 1 0 011-1h6a1 1 0 011 1v6a1 1 0 01-1 1H4a1 1 0 01-1-1v-6zM14 9a1 1 0 00-1 1v6a1 1 0 001 1h2a1 1 0 001-1v-6a1 1 0 00-1-1h-2z" clipRule="evenodd" />
                                    </svg>
                                </div>
                                <h4 className="font-medium text-gray-900">Medical Records</h4>
                                <p className="text-sm text-gray-600">Digital health history</p>
                            </div>

                            <div className="text-center">
                                <div className="text-yellow-500 mb-2">
                                    <svg className="w-8 h-8 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                    </svg>
                                </div>
                                <h4 className="font-medium text-gray-900">Prescriptions</h4>
                                <p className="text-sm text-gray-600">Digital prescriptions</p>
                            </div>

                            <div className="text-center">
                                <div className="text-red-500 mb-2">
                                    <svg className="w-8 h-8 mx-auto" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                    </svg>
                                </div>
                                <h4 className="font-medium text-gray-900">Secure</h4>
                                <p className="text-sm text-gray-600">HIPAA compliant</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;