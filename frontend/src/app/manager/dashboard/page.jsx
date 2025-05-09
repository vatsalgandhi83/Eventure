'use client';

import { useState } from 'react';
import Navbar from '@/components/Navbar';
import { Edit, Trash2, Calendar, MapPin, Ticket, DollarSign } from 'lucide-react';
import Link from 'next/link';

// Dummy data - in a real app, this would come from an API
const dummyEvents = [
  {
    id: '1',
    title: 'Summer Music Festival',
    date: '2024-07-15',
    time: '14:00',
    location: 'Central Park, New York',
    totalTickets: 1000,
    pricePerTicket: 50,
    ticketsSold: 750,
    status: 'active'
  },
  {
    id: '2',
    title: 'Tech Conference 2024',
    date: '2024-08-20',
    time: '09:00',
    location: 'Convention Center, San Francisco',
    totalTickets: 500,
    pricePerTicket: 200,
    ticketsSold: 300,
    status: 'active'
  },
  {
    id: '3',
    title: 'Food & Wine Expo',
    date: '2024-09-10',
    time: '11:00',
    location: 'Exhibition Hall, Chicago',
    totalTickets: 800,
    pricePerTicket: 75,
    ticketsSold: 800,
    status: 'sold-out'
  }
];

export default function ManagerDashboard() {
  const [events, setEvents] = useState(dummyEvents);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);

  const handleDelete = (eventId) => {
    setSelectedEvent(eventId);
    setShowDeleteModal(true);
  };

  const confirmDelete = () => {
    // In a real app, this would make an API call
    setEvents(events.filter(event => event.id !== selectedEvent));
    setShowDeleteModal(false);
    setSelectedEvent(null);
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'active':
        return 'bg-green-100 text-green-800';
      case 'sold-out':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold text-gray-900">My Events</h1>
          <Link
            href="/manager/events-create"
            className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Create New Event
          </Link>
        </div>

        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <ul className="divide-y divide-gray-200">
            {events.map((event) => (
              <li key={event.id}>
                <div className="px-4 py-4 sm:px-6">
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <div className="flex items-center">
                        <h2 className="text-lg font-medium text-gray-900">{event.title}</h2>
                        <span className={`ml-2 px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(event.status)}`}>
                          {event.status === 'sold-out' ? 'Sold Out' : 'Active'}
                        </span>
                      </div>
                      <div className="mt-2 sm:flex sm:justify-between">
                        <div className="sm:flex">
                          <div className="flex items-center text-sm text-gray-500 mr-4">
                            <Calendar className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                            {event.date} at {event.time}
                          </div>
                          <div className="flex items-center text-sm text-gray-500">
                            <MapPin className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                            {event.location}
                          </div>
                        </div>
                        <div className="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
                          <div className="flex items-center mr-4">
                            <Ticket className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                            {event.ticketsSold} / {event.totalTickets} sold
                          </div>
                          <div className="flex items-center">
                            <DollarSign className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" />
                            ${event.pricePerTicket}
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="ml-4 flex-shrink-0 flex space-x-2">
                      <Link
                        href={`/manager/events-edit/${event.id}`}
                        className="inline-flex items-center p-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                      >
                        <Edit className="h-5 w-5" />
                      </Link>
                      <button
                        onClick={() => handleDelete(event.id)}
                        className="inline-flex items-center p-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-red-700 bg-white hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                      >
                        <Trash2 className="h-5 w-5" />
                      </button>
                    </div>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>

        {/* Delete Confirmation Modal */}
        {showDeleteModal && (
          <div className="fixed z-10 inset-0 overflow-y-auto">
            <div className="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
              <div className="fixed inset-0 transition-opacity" aria-hidden="true">
                <div className="absolute inset-0 bg-gray-500 opacity-75"></div>
              </div>

              <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

              <div className="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
                <div>
                  <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100">
                    <Trash2 className="h-6 w-6 text-red-600" />
                  </div>
                  <div className="mt-3 text-center sm:mt-5">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">
                      Delete Event
                    </h3>
                    <div className="mt-2">
                      <p className="text-sm text-gray-500">
                        Are you sure you want to delete this event? This action cannot be undone.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="mt-5 sm:mt-6 sm:grid sm:grid-cols-2 sm:gap-3 sm:grid-flow-row-dense">
                  <button
                    type="button"
                    className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:col-start-2 sm:text-sm"
                    onClick={confirmDelete}
                  >
                    Delete
                  </button>
                  <button
                    type="button"
                    className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:col-start-1 sm:text-sm"
                    onClick={() => setShowDeleteModal(false)}
                  >
                    Cancel
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
