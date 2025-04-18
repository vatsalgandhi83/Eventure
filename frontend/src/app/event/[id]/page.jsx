'use client';

import { useState } from 'react';
import { useParams } from 'next/navigation';
import Navbar from '@/components/Navbar';
import { dummyEvents } from '@/data/dummyEvents';
import { Calendar, MapPin, User, Plus, Minus } from 'lucide-react';

export default function EventDetailsPage() {
  const { id } = useParams();
  const [ticketCount, setTicketCount] = useState(1);
  
  const event = dummyEvents.find(e => e.id === parseInt(id));
  
  if (!event) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <p>Event not found</p>
        </div>
      </div>
    );
  }

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  const handleTicketCountChange = (increment) => {
    const newCount = increment ? ticketCount + 1 : ticketCount - 1;
    if (newCount > 0 && newCount <= event.ticketsAvailable) {
      setTicketCount(newCount);
    }
  };

  const handleConfirmTicket = () => {
    // In a real application, this would make an API call
    console.log('Ticket order:', {
      eventId: event.id,
      ticketCount,
      totalPrice: event.price * ticketCount
    });
    alert(`Order confirmed! ${ticketCount} ticket(s) for ${event.title}`);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Event Image */}
          <div className="lg:col-span-2">
            <img
              src={event.image}
              alt={event.title}
              className="w-full h-96 object-cover rounded-lg"
            />
          </div>
          
          {/* Event Details */}
          <div className="space-y-6">
            <h1 className="text-3xl font-bold text-gray-900">{event.title}</h1>
            
            <div className="space-y-4">
              <div className="flex items-center">
                <Calendar className="h-5 w-5 text-gray-500 mr-2" />
                <span className="text-gray-700">
                  {formatDate(event.date)} at {event.time}
                </span>
              </div>
              
              <div className="flex items-center">
                <MapPin className="h-5 w-5 text-gray-500 mr-2" />
                <span className="text-gray-700">{event.location}</span>
              </div>
              
              <div className="flex items-center">
                <User className="h-5 w-5 text-gray-500 mr-2" />
                <span className="text-gray-700">Hosted by {event.host}</span>
              </div>
            </div>
            
            <div className="prose max-w-none">
              <h2 className="text-xl font-semibold text-gray-900">About this event</h2>
              <p className="text-gray-700">{event.description}</p>
            </div>
            
            {/* Map Placeholder */}
            <div className="bg-gray-200 h-64 rounded-lg flex items-center justify-center">
              <p className="text-gray-500">Map will be displayed here</p>
            </div>
          </div>
          
          {/* Ticket Section */}
          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Get Tickets</h2>
            
            {event.price > 0 ? (
              <div className="space-y-6">
                <div className="flex items-center justify-between">
                  <span className="text-gray-700">Price per ticket</span>
                  <span className="text-lg font-semibold">${event.price}</span>
                </div>
                
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
                
                <div className="border-t pt-4">
                  <div className="flex items-center justify-between mb-4">
                    <span className="text-gray-700">Total</span>
                    <span className="text-xl font-bold">${event.price * ticketCount}</span>
                  </div>
                  
                  <button
                    onClick={handleConfirmTicket}
                    className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                  >
                    Confirm Ticket
                  </button>
                </div>
              </div>
            ) : (
              <div className="space-y-4">
                <p className="text-gray-700">This is a free event!</p>
                <button
                  onClick={handleConfirmTicket}
                  className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  RSVP
                </button>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
} 