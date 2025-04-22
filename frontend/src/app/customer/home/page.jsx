'use client';

import { useState, useEffect } from 'react';
import EventCard from '@/components/EventCard';
import Navbar from '@/components/Navbar';
import Footer from '@/components/Footer';
import { Search, MapPin } from 'lucide-react';
import { useRouter } from 'next/navigation';

export default function HomePage() {
  const router = useRouter();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [locationQuery, setLocationQuery] = useState('');

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
          router.push('/login');
          return;
        }

        const response = await fetch('http://localhost:9000/api/events/', {
          headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
          }
        });
        console.log("Event Response",   response);

        if (!response.ok) {
          if (response.status === 401) {
            // Token expired or invalid, redirect to login
            localStorage.removeItem('accessToken');
            router.push('/login');
            return;
          }
          throw new Error('Failed to fetch events');
        }

        const data = await response.json();
        setEvents(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchEvents();
  }, [router]);

  const filteredEvents = events.filter(event => {
    const matchesSearch = event.eventName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         event.desc.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesLocation = event.city.toLowerCase().includes(locationQuery.toLowerCase()) ||
                          event.state.toLowerCase().includes(locationQuery.toLowerCase());
    return matchesSearch && matchesLocation;
  });

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="text-center">Loading events...</div>
        </div>
        <Footer />
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="text-center text-red-600">Error: {error}</div>
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Search and Filter Section */}
        <div className="mb-8 grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="relative">
            <input
              type="text"
              placeholder="Search events..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
          </div>
          
          <div className="relative">
            <input
              type="text"
              placeholder="Filter by location..."
              value={locationQuery}
              onChange={(e) => setLocationQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <MapPin className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
          </div>
        </div>

        {/* Events Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredEvents.length > 0 ? (
            filteredEvents.map((event) => (
              <EventCard key={event.id} event={event} />
            ))
          ) : (
            <div className="col-span-full text-center text-gray-500">
              No events found matching your search criteria.
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
} 