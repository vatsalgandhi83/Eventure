import Link from 'next/link';
import { Calendar, MapPin, User } from 'lucide-react';

export default function EventCard({ event }) {
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  const formatTime = (dateString) => {
    const options = { hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleTimeString(undefined, options);
  };

  return (
    <Link href={`/event/${event.id}`}>
      <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
        <div className="relative h-48">
          <img
            src="/event-placeholder.jpg" // You can add a default image or get it from the API
            alt={event.eventName}
            className="w-full h-full object-cover"
          />
          {event.ticketPrice === 0 && (
            <div className="absolute top-2 right-2 bg-green-500 text-white px-2 py-1 rounded-full text-xs font-semibold">
              FREE
            </div>
          )}
        </div>
        
        <div className="p-4">
          <h3 className="text-lg font-semibold text-gray-900 mb-2">{event.eventName}</h3>
          
          <div className="space-y-2 text-sm text-gray-600">
            <div className="flex items-center">
              <Calendar className="h-4 w-4 mr-2" />
              <span>{formatDate(event.eventDateTime)} at {formatTime(event.eventDateTime)}</span>
            </div>
            
            <div className="flex items-center">
              <MapPin className="h-4 w-4 mr-2" />
              <span>{event.address}</span>
            </div>
            
            <div className="flex items-center">
              <User className="h-4 w-4 mr-2" />
              <span>{event.available_tickets} tickets available</span>
            </div>
          </div>
          
          {event.ticketPrice > 0 && (
            <div className="mt-4">
              <span className="text-lg font-bold text-gray-900">
                ${event.ticketPrice.toFixed(2)}
              </span>
              <span className="text-sm text-gray-500"> / ticket</span>
            </div>
          )}
        </div>
      </div>
    </Link>
  );
} 