import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import Donate, { GET_DONATE_ITEMS } from '../Donate';
import '@testing-library/jest-dom';

// Mock data for testing
const mockDonateItems = [
    {
        __typename: 'DonateItem',
        donate: 'https://example.com/donate1',
        link: 'https://example.com/home1',
        img: 'red',
        alt: 'Item 1 Alt Text',
        name: 'Item 1',
        overview: 'This is an overview of Item 1.',
        founded: '2001',
        phone: '+123456789',
    },
    {
        __typename: 'DonateItem',
        donate: 'https://example.com/donate2',
        link: 'https://example.com/home2',
        img: 'blue',
        alt: 'Item 2 Alt Text',
        name: 'Item 2',
        overview: 'This is an overview of Item 2.',
        founded: '2010',
        phone: '+987654321',
    },
];

// Mock Apollo Client responses
const mocks = [
    {
        request: {
            query: GET_DONATE_ITEMS,
        },
        result: {
            data: {
                donateItems: mockDonateItems,
            },
        },
    },
];

// Mock Apollo Client with an error response
const errorMocks = [
    {
        request: {
            query: GET_DONATE_ITEMS,
        },
        error: new Error('Network error'),
    },
];

describe('Donate Component', () => {
    it('renders loading spinner initially', () => {
        render(
            <MockedProvider mocks={mocks} addTypename={true}>
                <Donate />
            </MockedProvider>
        );

        // Verify the loading spinner is displayed
        expect(screen.getByText(/Loading.../i)).toBeInTheDocument();
    });

    it('renders error message when GraphQL query fails', async () => {
        render(
            <MockedProvider mocks={errorMocks} addTypename={true}>
                <Donate />
            </MockedProvider>
        );

        // Wait for the error message to appear
        await waitFor(() => {
            expect(screen.getByText(/Error loading data: Network error/i)).toBeInTheDocument();
        });
    });

    it('renders donate items after successful fetch', async () => {
        render(
            <MockedProvider mocks={mocks} addTypename={true}>
                <Donate />
            </MockedProvider>
        );

        // Wait for the data to load
        await waitFor(() => {
            mockDonateItems.forEach((item) => {
                expect(screen.getByText(item.name)).toBeInTheDocument();
                expect(screen.getByText(new RegExp(`Founded in ${item.founded}`, 'i'))).toBeInTheDocument();
            });
        });
    });
});
