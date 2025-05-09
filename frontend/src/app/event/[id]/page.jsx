'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import Navbar from '@/components/Navbar';
import { Calendar, MapPin, User, Plus, Minus } from 'lucide-react';

export default function EventDetailsPage() {
  const { id } = useParams();
  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [ticketCount, setTicketCount] = useState(1);

  useEffect(() => {
    const fetchEventDetails = async () => {
      
      try {
        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
          router.push('/login');
          return;
        }
        
        const response = await fetch(`http://localhost:9000/api/events/${id}`, {
          headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
          },
          credentials: 'include' // ✅ only needed if backend uses cookies (e.g., session-based auth)
        });
        if (!response.ok) {
          throw new Error('Failed to fetch event details');
        }
        const data = await response.json();
        setEvent(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchEventDetails();
  }, [id]);

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleString(undefined, options);
  };

  const handleTicketCountChange = (increment) => {
    const newCount = increment ? ticketCount + 1 : ticketCount - 1;
    if (newCount > 0) {
      setTicketCount(newCount);
    }
  };

  const handleConfirmTicket = () => {
    // TODO: Implement ticket booking functionality
    alert(`Booking ${ticketCount} ticket(s) for ${event.eventName}`);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="animate-pulse space-y-4">
            <div className="h-96 bg-gray-200 rounded-lg"></div>
            <div className="h-8 bg-gray-200 rounded w-1/3"></div>
            <div className="space-y-2">
              <div className="h-4 bg-gray-200 rounded w-1/4"></div>
              <div className="h-4 bg-gray-200 rounded w-1/4"></div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (error || !event) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <p className="text-red-600">Error: {error || 'Event not found'}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Event Image */}
          <div className="lg:col-span-2">
            <img
              src={event.eventImage || '/placeholder-event.jpg'}
              alt={event.eventName}
              className="w-full h-96 object-cover rounded-lg"
            />
          </div>
          
          {/* Event Details */}
          <div className="space-y-6">
            <h1 className="text-3xl font-bold text-gray-900">{event.eventName}</h1>
            
            <div className="space-y-4">
              <div className="flex items-center">
                <Calendar className="h-5 w-5 text-gray-500 mr-2" />
                <span className="text-gray-700">
                  {formatDate(event.eventDateTime)}
                </span>
              </div>
              
              <div className="flex items-center">
                <MapPin className="h-5 w-5 text-gray-500 mr-2" />
                <span className="text-gray-700">
                  {typeof event.location === 'string' ? event.location : 'Location details available'}
                </span>
              </div>
              
              <div className="flex items-center">
                <User className="h-5 w-5 text-gray-500 mr-2" />
                <span className="text-gray-700">Hosted by {event.organizer}</span>
              </div>
            </div>
            
            <div className="prose max-w-none">
              <h2 className="text-xl font-semibold text-gray-900">About this event</h2>
              <p className="text-gray-700">{event.desc}</p>
            </div>
            
            {/* Additional Event Details */}
            <div className="space-y-4">
              {event.category && (
                <div className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full inline-block">
                  {event.category}
                </div>
              )}
              {event.maxAttendees && (
                <p className="text-gray-700">
                  Maximum Attendees: {event.maxAttendees}
                </p>
              )}
            </div>
          </div>
          
          {/* Ticket Section */}
          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Get Tickets</h2>
            
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-gray-700">Number of tickets</span>
                <div className="flex items-center space-x-4">
                  <button
                    onClick={() => handleTicketCountChange(false)}
                    className="p-2 rounded-full bg-gray-100 hover:bg-gray-200"
                  >
                    <Minus className="h-4 w-4" />
                  </button>
                  <span className="text-lg font-semibold">{ticketCount}</span>
                  <button
                    onClick={() => handleTicketCountChange(true)}
                    className="p-2 rounded-full bg-gray-100 hover:bg-gray-200"
                  >
                    <Plus className="h-4 w-4" />
                  </button>
                </div>
              </div>
              
              <button
                onClick={handleConfirmTicket}
                className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Reserve Spot
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
} 