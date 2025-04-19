'use client';

import { useState } from 'react';
import Navbar from '@/components/Navbar';
import { Calendar, MapPin, Ticket, DollarSign, X } from 'lucide-react';

// Dummy data - in a real app, this would come from an API
const dummyEvents = {
  upcoming: [
    {
      id: '1',
      title: 'Summer Music Festival',
      date: '2024-07-15',
      time: '14:00',
      location: 'Central Park, New York',
      ticketsBooked: 2,
      pricePerTicket: 50,
      totalPaid: 100,
      bookingId: 'B001'
    },
    {
      id: '2',
      title: 'Tech Conference 2024',
      date: '2024-08-20',
      time: '09:00',
      location: 'Convention Center, San Francisco',
      ticketsBooked: 1,
      pricePerTicket: 200,
      totalPaid: 200,
      bookingId: 'B002'
    }
  ],
  past: [
    {
      id: '3',
      title: 'Food & Wine Expo',
      date: '2024-03-10',
      time: '11:00',
      location: 'Exhibition Hall, Chicago',
      ticketsBooked: 3,
      pricePerTicket: 75,
      totalPaid: 225,
      bookingId: 'B003'
    },
    {
      id: '4',
      title: 'Spring Art Show',
      date: '2024-04-05',
      time: '10:00',
      location: 'Art Gallery, Boston',
      ticketsBooked: 2,
      pricePerTicket: 30,
      totalPaid: 60,
      bookingId: 'B004'
    }
  ]
};

export default function CustomerEvents() {
  const [events, setEvents] = useState(dummyEvents);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [selectedBooking, setSelectedBooking] = useState(null);

  const handleCancel = (bookingId) => {
    setSelectedBooking(bookingId);
    setShowCancelModal(true);
  };

  const confirmCancel = () => {
    // In a real app, this would make an API call
    setEvents({
      ...events,
      upcoming: events.upcoming.filter(event => event.bookingId !== selectedBooking)
    });
    setShowCancelModal(false);
    setSelectedBooking(null);
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  const EventCard = ({ event, isUpcoming }) => (
    <div className="bg-white shadow overflow-hidden sm:rounded-lg mb-4">
      <div className="px-4 py-5 sm:px-6">
        <div className="flex items-center justify-between">
          <div className="flex-1">
            <div className="flex items-center">
              <h2 className="text-lg font-medium text-gray-900">{event.title}</h2>
              {isUpcoming && (
                <span className="ml-2 px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                  Upcoming
                </span>
              )}
            </div>
            <div className="mt-2 sm:flex sm:justify-between">
              <div className="sm:flex">
                <div className="flex items-center text-sm text-gray-500 mr-4">
                  <Calendar className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                  {formatDate(event.date)} at {event.time}
                </div>
                <div className="flex items-center text-sm text-gray-500">
                  <MapPin className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                  {event.location}
                </div>
              </div>
              <div className="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
                <div className="flex items-center mr-4">
                  <Ticket className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                  {event.ticketsBooked} ticket{event.ticketsBooked > 1 ? 's' : ''}
                </div>
                <div className="flex items-center">
                  <DollarSign className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                  ${event.totalPaid}
                </div>
              </div>
            </div>
          </div>
          {isUpcoming && (
            <div className="ml-4 flex-shrink-0">
              <button
                onClick={() => handleCancel(event.bookingId)}
                className="inline-flex items-center p-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-red-700 bg-white hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
              >
                <X className="h-5 w-5" />
                <span className="ml-2">Cancel Booking</span>
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-8">My Events</h1>

        {/* Upcoming Events Section */}
        <div className="mb-12">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Upcoming Events</h2>
          {events.upcoming.length > 0 ? (
            events.upcoming.map(event => (
              <EventCard key={event.bookingId} event={event} isUpcoming={true} />
            ))
          ) : (
            <p className="text-gray-500">No upcoming events</p>
          )}
        </div>

        {/* Past Events Section */}
        <div>
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Past Events</h2>
          {events.past.length > 0 ? (
            events.past.map(event => (
              <EventCard key={event.bookingId} event={event} isUpcoming={false} />
            ))
          ) : (
            <p className="text-gray-500">No past events</p>
          )}
        </div>

        {/* Cancel Confirmation Modal */}
        {showCancelModal && (
          <div className="fixed z-10 inset-0 overflow-y-auto">
            <div className="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
              <div className="fixed inset-0 transition-opacity" aria-hidden="true">
                <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
              </div>

              <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

              <div className="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
                <div>
                  <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100">
                    <X className="h-6 w-6 text-red-600" />
                  </div>
                  <div className="mt-3 text-center sm:mt-5">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">
                      Cancel Booking
                    </h3>
                    <div className="mt-2">
                      <p className="text-sm text-gray-500">
                        Are you sure you want to cancel this booking? This action cannot be undone.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="mt-5 sm:mt-6 sm:grid sm:grid-cols-2 sm:gap-3 sm:grid-flow-row-dense">
                  <button
                    type="button"
                    className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:col-start-2 sm:text-sm"
                    onClick={confirmCancel}
                  >
                    Cancel Booking
                  </button>
                  <button
                    type="button"
                    className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:col-start-1 sm:text-sm"
                    onClick={() => setShowCancelModal(false)}
                  >
                    Keep Booking
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}
