import React, { useState, useEffect } from 'react';
import Navbar from './Navbar';

const Profile = () => {
  const [userId, setUserId] = useState('123'); // Replace with actual user ID retrieval logic
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNo, setPhoneNo] = useState('');
  const [password, setPassword] = useState('');

  const BASE_URL = "http://localhost:9000/api/user";

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`${BASE_URL}/${userId}`);
        if (!response.ok) {
          throw new Error('Failed to fetch user data');
        }
        const data = await response.json();
        setFirstName(data.firstName);
        setLastName(data.lastName);
        setEmail(data.email);
        setPhoneNo(data.phoneNo);
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };
    fetchUserData();
  }, [userId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`${BASE_URL}/${userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ firstName, lastName, email, phoneNo, password }),
      });

      if (!response.ok) {
        console.error('Submission failed:', response.statusText);
      } else {
        const result = await response.json();
        console.log('Profile updated successfully:', result);
      }
    } catch (error) {
      console.error('Error submitting the form:', error);
    }
  };

  const handleDelete = async () => {
    try {
      const response = await fetch(`${BASE_URL}/${userId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        console.error('Failed to delete profile:', response.statusText);
      } else {
        console.log('Profile deleted successfully');
      }
    } catch (error) {
      console.error('Error deleting profile:', error);
    }
  };

  return (
    <>
      <Navbar/>
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="bg-white p-8 rounded shadow-md w-full max-w-md">
          <h2 className="text-2xl font-bold mb-6 text-center">Profile Form</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="block text-gray-700">First Name</label>
              <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded" />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700">Last Name</label>
              <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded" />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700">Email</label>
              <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded" />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700">Phone Number</label>
              <input type="text" value={phoneNo} onChange={(e) => setPhoneNo(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded" />
            </div>
            <div className="mb-6">
              <label className="block text-gray-700">Password</label>
              <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded" />
            </div>
            <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition duration-200">Submit</button>
          </form>
          <button onClick={handleDelete} className="mt-4 w-full bg-red-500 text-white py-2 rounded hover:bg-red-600 transition duration-200">Delete Profile</button>
        </div>
      </div>
    </>
  );
}

export default Profile;
