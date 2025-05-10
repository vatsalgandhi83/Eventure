'use client';

import { useState, useEffect } from 'react';
import { Search, MapPin, Menu, X, LogOut } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

export default function Navbar() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState('');
  const router = useRouter();

  useEffect(() => {
    // Check if user is logged in and get their role
    const token = localStorage.getItem('accessToken');
    const role = localStorage.getItem('userRole');
    setIsLoggedIn(!!token);
    setUserRole(role || '');
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRole');
    setIsLoggedIn(false);
    setUserRole('');
    router.push('/login');
  };

  const renderAuthButtons = () => {
    if (isLoggedIn) {
      return (
        <button
          onClick={handleLogout}
          className="px-3 py-2 border border-gray-300 rounded-lg text-gray-600 hover:text-gray-900 flex items-center gap-2"
        >
          <LogOut className="h-4 w-4" />
          Logout
        </button>
      );
    }

    return (
      <>
        <Link
          href="/login"
          className="px-3 py-2 border border-gray-300 rounded-lg text-gray-600 hover:text-gray-900"
        >
          Login
        </Link>
        <Link
          href="/signup"
          className="px-3 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
        >
          Signup
        </Link>
      </>
    );
  };

  const renderNavigationLinks = () => {
    if (!isLoggedIn) return null;

    if (userRole === 'Manager') {
      return (
        <div className="flex items-center space-x-4">
          <Link href="/manager/events" className="text-gray-600 hover:text-gray-900">
            My Events
          </Link>
          <Link href="/manager/profile" className="text-gray-600 hover:text-gray-900">
            Profile
          </Link>
        </div>
      );
    }

    // Default to Customer view
    return (
      <div className="flex items-center space-x-4">
        <Link href="/customer/tickets" className="text-gray-600 hover:text-gray-900">
          My Tickets
        </Link>
        <Link href="/customer/profile" className="text-gray-600 hover:text-gray-900">
          Profile
        </Link>
      </div>
    );
  };

  const renderMobileNavigationLinks = () => {
    if (!isLoggedIn) return null;

    if (userRole === 'Manager') {
      return (
        <>
          <Link
            href="/manager/events"
            className="block px-3 py-2 text-gray-600 hover:text-gray-900"
          >
            My Events
          </Link>
          <Link
            href="/manager/profile"
            className="block px-3 py-2 text-gray-600 hover:text-gray-900"
          >
            Profile
          </Link>
        </>
      );
    }

    // Default to Customer view
    return (
      <>
        <Link
          href="/customer/tickets"
          className="block px-3 py-2 text-gray-600 hover:text-gray-900"
        >
          My Tickets
        </Link>
        <Link
          href="/customer/profile"
          className="block px-3 py-2 text-gray-600 hover:text-gray-900"
        >
          Profile
        </Link>
      </>
    );
  };

  return (
    <nav className="bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link 
              href={isLoggedIn ? (userRole === 'Manager' ? '/manager/home' : '/customer/home') : '/'} 
              className="text-xl font-bold text-gray-800"
            >
              Eventure
            </Link>
          </div>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center space-x-4">
            <div className="relative">
              <input
                type="text"
                placeholder="Search events..."
                className="pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </div>
            
            <div className="relative">
              <input
                type="text"
                placeholder="Location"
                className="pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <MapPin className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </div>

            {renderNavigationLinks()}

            <div className="flex items-center space-x-4">
              {renderAuthButtons()}
            </div>
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden flex items-center">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              className="text-gray-500 hover:text-gray-900"
            >
              {isMenuOpen ? (
                <X className="h-6 w-6" />
              ) : (
                <Menu className="h-6 w-6" />
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Navigation */}
      {isMenuOpen && (
        <div className="md:hidden">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
            <div className="relative mb-4">
              <input
                type="text"
                placeholder="Search events..."
                className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </div>
            
            <div className="relative mb-4">
              <input
                type="text"
                placeholder="Location"
                className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <MapPin className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </div>

            {renderMobileNavigationLinks()}

            {isLoggedIn ? (
              <button
                onClick={handleLogout}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg text-gray-600 hover:text-gray-900 flex items-center justify-center gap-2"
              >
                <LogOut className="h-4 w-4" />
                Logout
              </button>
            ) : (
              <>
                <Link
                  href="/login"
                  className="block px-3 py-2 border border-gray-300 rounded-lg text-gray-600 hover:text-gray-900"
                >
                  Login
                </Link>
                <Link
                  href="/signup"
                  className="block px-3 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                >
                  Signup
                </Link>
              </>
            )}
          </div>
        </div>
      )}
    </nav>
  );
}
