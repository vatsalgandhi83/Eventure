import Link from 'next/link';
import { Calendar, MapPin, User } from 'lucide-react';

export default function EventCard({ event }) {
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };

  return (
    <Link href={`/event/${event.id}`}>
      <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
        <div className="relative h-48">
          <img
            src={event.image}
            alt={event.title}
            className="w-full h-full object-cover"
          />
          {event.price === 0 && (
            <div className="absolute top-2 right-2 bg-green-500 text-white px-2 py-1 rounded-full text-xs font-semibold">
              FREE
            </div>
          )}
        </div>
        
        <div className="p-4">
          <h3 className="text-lg font-semibold text-gray-900 mb-2">{event.title}</h3>
          
          <div className="space-y-2 text-sm text-gray-600">
            <div className="flex items-center">
              <Calendar className="h-4 w-4 mr-2" />
              <span>{formatDate(event.date)} at {event.time}</span>
            </div>
            
            <div className="flex items-center">
              <MapPin className="h-4 w-4 mr-2" />
              <span>{event.location}</span>
            </div>
            
            <div className="flex items-center">
              <User className="h-4 w-4 mr-2" />
              <span>Hosted by {event.host}</span>
            </div>
          </div>
          
          {event.price > 0 && (
            <div className="mt-4">
              <span className="text-lg font-bold text-gray-900">
                ${event.price}
              </span>
              <span className="text-sm text-gray-500"> / ticket</span>
            </div>
          )}
        </div>
      </div>
    </Link>
  );
} 