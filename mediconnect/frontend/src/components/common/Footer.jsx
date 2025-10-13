import React from 'react';

const Footer = () => {
    const currentYear = new Date().getFullYear();

    return (
        <footer className="bg-gray-800 text-white mt-auto">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    {/* Company Info */}
                    <div>
                        <h3 className="text-lg font-semibold mb-4">MediConnect</h3>
                        <p className="text-gray-300 text-sm">
                            Connecting patients with healthcare professionals for better medical care and seamless appointment management.
                        </p>
                    </div>

                    {/* Quick Links */}
                    <div>
                        <h3 className="text-lg font-semibold mb-4">Quick Links</h3>
                        <ul className="space-y-2 text-sm">
                            <li><a href="/patient-login" className="text-gray-300 hover:text-white transition-colors">Patient Portal</a></li>
                            <li><a href="/doctor-login" className="text-gray-300 hover:text-white transition-colors">Doctor Portal</a></li>
                            <li><a href="/admin-login" className="text-gray-300 hover:text-white transition-colors">Admin Panel</a></li>
                            <li><a href="/register" className="text-gray-300 hover:text-white transition-colors">Register Patient</a></li>
                        </ul>
                    </div>

                    {/* Contact Info */}
                    <div>
                        <h3 className="text-lg font-semibold mb-4">Contact</h3>
                        <div className="text-sm text-gray-300 space-y-2">
                            <p>📧 support@mediconnect.com</p>
                            <p>📞 +1 (555) 123-4567</p>
                            <p>📍 123 Healthcare Ave, Medical City</p>
                            <p>🕒 24/7 Emergency Support</p>
                        </div>
                    </div>
                </div>

                <div className="border-t border-gray-700 mt-8 pt-6 text-center">
                    <p className="text-sm text-gray-300">
                        © {currentYear} MediConnect. All rights reserved. |
                        <a className="hover:text-white ml-1">Privacy Policy</a> |
                        <a className="hover:text-white ml-1">Terms of Service</a>
                    </p>
                </div>
            </div>
        </footer>
    );
};

export default Footer;