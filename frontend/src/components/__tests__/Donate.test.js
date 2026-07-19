import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import Donate, { GET_DONATE_ITEMS, DONATE_ADDED_SUBSCRIPTION } from '../Donate';
import '@testing-library/jest-dom';
import { trackClickOut } from '../../utils/activityTracker';

jest.mock('../../utils/activityTracker', () => ({
    trackClickOut: jest.fn(),
}));

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

// Mock Apollo Client responses including the subscription mock
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
    {
        request: {
            query: DONATE_ADDED_SUBSCRIPTION,
        },
        result: {
            data: {
                donateItemsSubscription: {
                    __typename: 'DonateItem',
                    donate: 'https://example.com/donate3',
                    link: 'https://example.com/home3',
                    img: 'green',
                    alt: 'Item 3 Alt Text',
                    name: 'Item 3',
                    overview: 'This is an overview of Item 3.',
                    founded: '2020',
                    phone: '+111111111',
                },
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
            <MockedProvider mocks={mocks}>
                <Donate />
            </MockedProvider>
        );

        // Verify the loading spinner is displayed
        expect(screen.getByText(/Loading.../i)).toBeInTheDocument();
    });

    it('renders error message when GraphQL query fails', async () => {
        render(
            <MockedProvider mocks={errorMocks}>
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
            <MockedProvider mocks={mocks}>
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

    it('tracks charity click-outs for donate and homepage links', async () => {
        render(
            <MockedProvider mocks={mocks}>
                <Donate />
            </MockedProvider>
        );

        await waitFor(() => {
            expect(screen.getByText('Item 1')).toBeInTheDocument();
        });

        const donateLink = document.querySelector('a[href="https://example.com/donate1"]');
        const homepageLink = document.querySelector('a[href="https://example.com/home1"]');
        expect(donateLink).toBeInTheDocument();
        expect(homepageLink).toBeInTheDocument();

        fireEvent.click(donateLink);
        fireEvent.click(homepageLink);

        expect(trackClickOut).toHaveBeenCalledWith('charity', 'https://example.com/donate1');
        expect(trackClickOut).toHaveBeenCalledWith('charity', 'https://example.com/home1');
    });

    it('appends newly added charity to list on subscription update', async () => {
        render(
            <MockedProvider mocks={mocks}>
                <Donate />
            </MockedProvider>
        );

        // Wait for the original query items to render
        await waitFor(() => {
            expect(screen.getByText('Item 1')).toBeInTheDocument();
        });

        // Verify that the subscription updates state and appends the new charity to the carousel list
        await waitFor(() => {
            expect(screen.getByText('Item 3')).toBeInTheDocument();
            expect(screen.getByText(/Founded in 2020/i)).toBeInTheDocument();
        });
    });
});
